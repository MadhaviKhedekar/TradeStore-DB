package com.example.tradestorage.service;

import com.example.tradestorage.model.Trade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TradeService {
    public List<Trade> findAll();

    void persist(Trade trade);

    void storeTrade(Trade trade);

    void updateExpiryFlagOfTrade();
}
