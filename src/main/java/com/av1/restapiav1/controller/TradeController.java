package com.av1.restapiav1.controller;

import com.av1.restapiav1.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    private TradeService service;

    @GetMapping("/iniciar")
    public void Iniciar(){
        service.Iniciar();
    }
}
