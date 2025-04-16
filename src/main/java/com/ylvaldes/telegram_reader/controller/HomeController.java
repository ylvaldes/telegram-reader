package com.ylvaldes.telegram_reader.controller;

import com.ylvaldes.telegram_reader.model.UrlDTOFull;
import com.ylvaldes.telegram_reader.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class HomeController {

    @Autowired
    UrlService urlService;

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Hello World!";
    }

}
