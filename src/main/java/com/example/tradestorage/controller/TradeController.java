package com.example.tradestorage.controller;

import com.example.tradestorage.exceptionHandler.InvalidTradeException;
import com.example.tradestorage.model.Trade;
import com.example.tradestorage.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TradeController {
    @Autowired
    TradeService tradeService;

    @PostMapping("/trade")
    public ResponseEntity<String> storeTrade(@RequestBody Trade trade){
        tradeService.storeTrade(trade);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/allTrades")
    public List<Trade> findAllTrades(){
        return tradeService.findAll();
    }
}

