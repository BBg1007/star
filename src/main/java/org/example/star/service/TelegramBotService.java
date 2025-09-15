package org.example.star.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.star.constants.TgBotCommandType;
import org.example.star.model.recomendation.Recommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

@Service
public class TelegramBotService {

    @Autowired
    RecommendationService recommendationService;
    @Lazy
    @Autowired
    private TelegramBot telegramBot;

    public void handleUpdate(Update update) {
        String text = update.message().text();
        long chatId = update.message().chat().id();
        if (!recommendationService.isUserOfTgBot(chatId)) {
            doFirstVisitLogic(chatId);
        }
        handleMessage(text,chatId);

    }

    private void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId,text);
        try {
            telegramBot.execute(sendMessage);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }



    private String extractMessageText(String messageText, TgBotCommandType type) {
        return messageText.substring(type.getType().length()).trim();
    }

    private TgBotCommandType getCommandTypeFromText(String Text) {
        TgBotCommandType type = TgBotCommandType.checkCommandType(Text);
        if(type.equals(TgBotCommandType.NOT_FOUND)){
            throw new IllegalArgumentException("Команда не распознана.");
        }
        return type;
    }



    private void doFirstVisitLogic(long chatId) {
        String text = "Приветствую, это бот банка «Star»!\n" +
                "Готов дать Вам самые выгодные рекомендации!\n" +
                "Для этого нужно написать команду /recommend + Ваше имя и фамилию\n" +
                "Например: /recommend Иван Иванов";
        sendMessage(chatId,text);
        recommendationService.markAsVisitor(chatId);
    }

    private void handleMessage(String text,long chatId) {
       try {
           TgBotCommandType commandType = getCommandTypeFromText(text);
           String commandText = extractMessageText(text,commandType);
           Object response = executeCommand(commandType,commandText);
           sendResponse(chatId,response);
       } catch (IllegalArgumentException e) {
           System.out.println(e.getMessage());
       }


    }

    private Object executeCommand(TgBotCommandType commandType, String commandText) {
         switch (commandType) {
             case RECOMMEND: return executeRecommendCommand(commandText);
         }
         return null;
    }

    private List<Recommendation> executeRecommendCommand(String commandText) {
        return recommendationService.getRecommendationByUserName( commandText);
    }

    private void sendResponse(long chatId,Object response) {
        if (response instanceof List && !response.equals(Collections.EMPTY_LIST)) {
            List<Recommendation> recommendations = (List<Recommendation>) response;
            for (Recommendation recommendation : recommendations) {
                sendMessage(chatId,recommendation.toString());
            }
        }else sendMessage(chatId,"Пользователь не найден.");
    }




}
