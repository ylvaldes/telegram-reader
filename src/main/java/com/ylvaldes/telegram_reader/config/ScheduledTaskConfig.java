/*
package com.ylvaldes.telegram_reader.config;

import com.ylvaldes.telegram_reader.service.ProcesarDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class ScheduledTaskConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${app.task.enable:false}")
    private Boolean taskEnable;

    @Value("${app.task.expression:0_0_3_*_*_?}")
    public String taskExpression;
    @Autowired
    private ProcesarDataService procesarDataService;


    @Scheduled(cron = "#{taskExpression.replace('_', ' ')}", initialDelay = 5 * 1000)
    public void procesarPendientes() {
        logger.info(getClass().getSimpleName() + " - " + Thread.currentThread().getStackTrace()[1].getMethodName() + " - Begin;");
        if (taskEnable) {

        }
        logger.info(getClass().getSimpleName() + " - " + Thread.currentThread().getStackTrace()[1].getMethodName() + " - End;");
    }

}

*/
