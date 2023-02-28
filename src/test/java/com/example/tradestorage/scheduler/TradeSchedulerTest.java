package com.example.tradestorage.scheduler;

import com.example.tradestorage.TradestorageApplication;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig(TradestorageApplication.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradeSchedulerTest {

    @SpyBean
    private TradeScheduler tradeScheduler;

    @Test
    public void whenWaitOneSecond_thenScheduledIsCalledAtLeastTenTimes() {
        await()
                .atMost(Duration.TEN_MINUTES)
                .untilAsserted(() -> verify(tradeScheduler, atLeast(1)).cronJobScheduler());
    }

}
