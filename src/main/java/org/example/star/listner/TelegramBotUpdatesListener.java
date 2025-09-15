package org.example.star.listner;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.star.service.TelegramBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Provider;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final TelegramBotService telegramBotService;

    public TelegramBotUpdatesListener(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {

            if (update.message() != null && update.message().text()!=null) {
               telegramBotService.handleUpdate(update);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }




}
