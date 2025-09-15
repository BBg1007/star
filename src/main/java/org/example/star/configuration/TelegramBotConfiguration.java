package org.example.star.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import org.example.star.listner.TelegramBotUpdatesListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class TelegramBotConfiguration {

    @Value("${telegram.bot.token}")
    private String token;

    @Bean
    public TelegramBot telegramBot(@Lazy TelegramBotUpdatesListener updatesListener) {
        TelegramBot bot = new TelegramBot(token);

        // Регистрируем обработчик updates
        bot.setUpdatesListener(updatesListener);

        bot.execute(new DeleteMyCommands());
        return bot;
    }
}
