package com.example.tradestorage.scheduler;

import com.example.tradestorage.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TradeScheduler {
    private static final Logger log = LoggerFactory.getLogger(TradeScheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    TradeService tradeService;

    //cron job is currently configured for every 5 mins. We can configure is for daily with "0 0 0 1-31 * ?"
   @Scheduled(cron = "${trade.expiry.schedule}")
    public void cronJobScheduler() {
        log.info("the current job is executing at ", dateFormat.format(new Date()));
        tradeService.updateExpiryFlagOfTrade();
    }
}
