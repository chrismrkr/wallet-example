package com.example.wallet_example1.balance.medium.infrastructure.kafka;

import com.example.wallet_example1.balance.domain.BalanceEvent;
import com.example.wallet_example1.balance.service.port.BalanceEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
public class BalanceEventPubSubTest {
    @Autowired
    BalanceEventPublisher eventPublisher;
    @Autowired
    Environment environment;

    @Test
    void 단건_Pub_Sub_성공() throws InterruptedException {
        // given
        BalanceEvent event = BalanceEvent.builder()
                .eventId(1L)
                .senderBalanceId("123L")
                .amount("10000")
                .build();
        // when
        eventPublisher.publishEvent(environment.getProperty("spring.kafka.topic.balance.topic-name"), "123", event);
        // then
        Thread.sleep(2000L);

    }
}
