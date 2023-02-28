package com.example.tradestorage;

import com.example.tradestorage.controller.TradeController;
import com.example.tradestorage.exceptionHandler.InvalidTradeException;
import com.example.tradestorage.model.Trade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional(propagation= Propagation.REQUIRES_NEW)
class TradestorageApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private TradeController tradeController;

    @Test
    void testStoreTradeSuccess() {
        ResponseEntity responseEntity = tradeController.storeTrade(createTrade("T1",1,LocalDate.now()));
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity);
        List<Trade> tradeList =tradeController.findAllTrades();
        Assertions.assertEquals(1, tradeList.size());
        Assertions.assertEquals("T1",tradeList.get(0).getTradeId());
    }


    @Test
    void testTradeCreationForMaturityDateInPast() {
        try {
            LocalDate localDate = getLocalDate(2022, 05, 21);
            ResponseEntity responseEntity = tradeController.storeTrade(createTrade("T2", 1, localDate));
        } catch (InvalidTradeException ie) {
            Assertions.assertEquals("Invalid Trade: T2 due to Invalid maturity date, cant be older than today", ie.getMessage());
        }
    }

    @Test
    void testTradeCreationForMaturityDateInFuture() {
        ResponseEntity responseEntity = tradeController.storeTrade(createTrade("T3", 1, LocalDate.MAX));
        List<Trade> tradeList = tradeController.findAllTrades();
        Assertions.assertEquals(1, tradeList.size());
        Assertions.assertEquals("T3", tradeList.get(0).getTradeId());
    }

    @Test
    void testTradeCreationForOlderVersion() {
        // create trade with newer version
        ResponseEntity responseEntity = tradeController.storeTrade(createTrade("T1", 2, LocalDate.now()));
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);
        List<Trade> tradeList = tradeController.findAllTrades();
        Assertions.assertEquals(1, tradeList.size());
        Assertions.assertEquals("T1", tradeList.get(0).getTradeId());
        Assertions.assertEquals(2, tradeList.get(0).getVersion());
        Assertions.assertEquals("T1B1", tradeList.get(0).getBookId());

        //create trade with old version
        try {
            ResponseEntity responseEntity1 = tradeController.storeTrade(createTrade("T1", 1, LocalDate.now()));
        } catch (InvalidTradeException e) {
            Assertions.assertEquals("Invalid Trade: T1 due to User has provided lower trade version than expected", e.getMessage());
        }
        List<Trade> tradeList1 = tradeController.findAllTrades();
        Assertions.assertEquals(1, tradeList1.size());
        Assertions.assertEquals("T1", tradeList1.get(0).getTradeId());
        Assertions.assertEquals(2, tradeList1.get(0).getVersion());
        Assertions.assertEquals("T1B1", tradeList.get(0).getBookId());
    }

    @Test
    void testTradeCreationForSameVersion() {
        ResponseEntity responseEntity = tradeController.storeTrade(createTrade("T1", 2, LocalDate.now()));
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);
        List<Trade> tradeList = tradeController.findAllTrades();
        Assertions.assertEquals(1, tradeList.size());
        Assertions.assertEquals("T1", tradeList.get(0).getTradeId());
        Assertions.assertEquals(2, tradeList.get(0).getVersion());
        Assertions.assertEquals("T1B1", tradeList.get(0).getBookId());

        //create trade with same version
        Trade trade2 = createTrade("T1", 2, LocalDate.now());
        trade2.setBookId("T1B1V2");
        ResponseEntity responseEntity2 = tradeController.storeTrade(trade2);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity2);
        List<Trade> tradeList2 = tradeController.findAllTrades();
        Assertions.assertEquals(1, tradeList2.size());
        Assertions.assertEquals("T1", tradeList2.get(0).getTradeId());
        Assertions.assertEquals(2, tradeList2.get(0).getVersion());
        Assertions.assertEquals("T1B1V2", tradeList2.get(0).getBookId());

        //create trade with same version
        Trade trade3 = createTrade("T1", 2, LocalDate.now());
        trade3.setBookId("T1B1V3");
        ResponseEntity responseEntity3 = tradeController.storeTrade(trade3);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity3);
        List<Trade> tradeList3 = tradeController.findAllTrades();
        Assertions.assertEquals(1, tradeList3.size());
        Assertions.assertEquals("T1", tradeList3.get(0).getTradeId());
        Assertions.assertEquals(2, tradeList3.get(0).getVersion());
        Assertions.assertEquals("T1B1V3", tradeList3.get(0).getBookId());

    }

    @Test
    void testTradeCreationForDifferentVersion() {
        ResponseEntity responseEntity = tradeController.storeTrade(createTrade("T1", 2, LocalDate.now()));
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);
        List<Trade> tradeList = tradeController.findAllTrades();
        Assertions.assertEquals(1, tradeList.size());
        Assertions.assertEquals("T1", tradeList.get(0).getTradeId());
        Assertions.assertEquals(2, tradeList.get(0).getVersion());
        Assertions.assertEquals("T1B1", tradeList.get(0).getBookId());

        //create trade with new version
        Trade trade2 = createTrade("T1", 3, LocalDate.now());
        trade2.setBookId("T1B1V2");
        ResponseEntity responseEntity2 = tradeController.storeTrade(trade2);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity2);
        List<Trade> tradeList2 = tradeController.findAllTrades();
        Assertions.assertEquals(1, tradeList2.size());
        Assertions.assertEquals("T1", tradeList.get(0).getTradeId());
        Assertions.assertEquals(3, tradeList.get(0).getVersion());
        Assertions.assertEquals("T1B1V2", tradeList.get(0).getBookId());

    }

    private Trade createTrade(String tradeId, int version, LocalDate maturityDate) {
        Trade trade = new Trade();
        trade.setTradeId(tradeId);
        trade.setBookId(tradeId + "B1");
        trade.setVersion(version);
        trade.setCounterPartyId("CP-" + version);
        trade.setCreatedDate(LocalDate.now());
        trade.setMaturityDate(maturityDate);
        trade.setExpired("N");
        return trade;
    }

    public static LocalDate getLocalDate(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month, day);
        return localDate;
    }



    }
