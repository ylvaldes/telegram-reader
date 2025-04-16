package com.ylvaldes.telegram_reader.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author YasmaniLedesmaValdez
 * @project telegram-reader
 * @package com.ylvaldes.telegram_reader.service
 * @created 22/4/2023
 * @implNote
 */
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ProcesarURLService procesarDataService;
    @Value("${telegramBot.name}")
    private String botName;

    @Value("${telegramBot.token}")
    private String bottoken;

    @Value("${telegramBot.owner}")
    private String botOwnerId;

    @Override
    public void onUpdateReceived(Update update) {
        // Se obtiene el mensaje escrito por el usuario
        final String messageTextReceived = update.getMessage().getText();
        logger.info("Mensaje recibido: {}", messageTextReceived);

        String messageQa = messageTextReceived.replace("%2F", "/");
        String respuesta = procesarDataService.procesarDatos(messageQa);
        // Se obtiene el id de chat del usuario

        final long chatId = update.getMessage().getChatId();

        // Se crea un objeto mensaje
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Estamos procesando tu " + respuesta);

        try {
            // Se envía el mensaje
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return bottoken;
    }
}
