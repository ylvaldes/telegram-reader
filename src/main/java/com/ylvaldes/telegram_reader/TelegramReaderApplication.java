package com.ylvaldes.telegram_reader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {
        "com.ylvaldes.telegram_reader",
        "org.telegram.telegrambots"
})
public class TelegramReaderApplication {

    public static void main(final String[] args) {
        SpringApplication.run(TelegramReaderApplication.class, args);
    }

}
