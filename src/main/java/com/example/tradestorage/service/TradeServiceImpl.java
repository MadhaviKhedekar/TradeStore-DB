package com.example.tradestorage.service;

import com.example.tradestorage.dao.TradeDao;
import com.example.tradestorage.exceptionHandler.InvalidTradeException;
import com.example.tradestorage.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class TradeServiceImpl implements TradeService {
    private static final Logger log = LoggerFactory.getLogger(TradeServiceImpl.class);

    @Autowired
    TradeDao tradeDao;

    @Override
    public void storeTrade(Trade trade) {
        if (verifyMaturityDate((trade))) {
            Optional<Trade> exsitingTrade = tradeDao.findById(trade.getTradeId());
            if (!exsitingTrade.isPresent() || (exsitingTrade.isPresent() && verifyVersion(trade, exsitingTrade.get()))) {
                persist(trade);
            } else {
                throw new InvalidTradeException(trade.getTradeId(), "User has provided lower trade version than expected");
            }
        } else {
            throw new InvalidTradeException(trade.getTradeId(), "Invalid maturity date, cant be older than today");
        }
    }


    //verify if trade version is not lower than existing entry for same trade id
    private boolean verifyVersion(Trade currentTrade, Trade oldTrade) {
        if (currentTrade.getVersion() >= oldTrade.getVersion()) {
            return true;
        }
        return false;
    }

    //veriy if maturity date is not older than today
    private boolean verifyMaturityDate(Trade currentTrade) {
        return currentTrade.getMaturityDate().isBefore(LocalDate.now()) ? false : true;
    }

    public void persist(Trade trade) {
        trade.setCreatedDate(LocalDate.now());
        tradeDao.save(trade);
    }


    public List<Trade> findAll() {
        return tradeDao.findAll();
    }

    public void updateExpiryFlagOfTrade() {
        log.info("calling updateExpiryFlagOfTrade");
        List<Trade> tradeList = findAll();
        if (!tradeList.isEmpty()) {
            tradeDao.findAll().stream().forEach(t -> {
                if (!verifyMaturityDate(t)) {
                    t.setExpired("Y");
                    tradeDao.save(t);
                }
            });
        } else {
            log.info("No data exists to update expiry flag");
        }
    }


}
