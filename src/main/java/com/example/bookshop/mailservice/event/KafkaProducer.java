package com.example.bookshop.mailservice.event;

import com.example.bookshop.mailservice.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private KafkaTemplate<String, String> accountConfirmedTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> accountConfirmedTemplate) {
        this.accountConfirmedTemplate = accountConfirmedTemplate;
    }

    public void notifyAccountConfirmed(String email) {
        accountConfirmedTemplate.send(CommonConstants.KAFKA_TOPIC_ACCOUNT_CONFIRMED, email);
        System.out.println("SENT ACCOUNT_CONFIRMED EVENT WITH " + email + " FOR DATA");
    }
}
