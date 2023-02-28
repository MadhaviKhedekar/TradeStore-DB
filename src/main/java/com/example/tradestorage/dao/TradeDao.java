package com.example.tradestorage.dao;

import com.example.tradestorage.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeDao extends JpaRepository<Trade,String> {
}
