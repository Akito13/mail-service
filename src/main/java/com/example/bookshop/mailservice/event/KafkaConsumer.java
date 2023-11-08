package com.example.bookshop.mailservice.event;

import com.example.bookshop.mailservice.model.AccountConfirmation;
import com.example.bookshop.mailservice.service.MailService;
import com.example.bookshop.mailservice.utils.Randomizer;
import jakarta.mail.MessagingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class KafkaConsumer {
    private final MailService mailService;
    private final CopyOnWriteArrayList<AccountConfirmation> pendingConfirmation;

    @Autowired
    public KafkaConsumer(MailService mailService, CopyOnWriteArrayList<AccountConfirmation> pendingConfirmation) {
        this.mailService = mailService;
        this.pendingConfirmation = pendingConfirmation;
    }

    @KafkaListener(
            topics = "${constant.kafka.account-registration}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeAccountRegistration(ConsumerRecord<String, String> record) throws MessagingException {
        String email = record.value();
        String randomCode = Randomizer.random();
        mailService.send(email, "XÁC NHẬN TÀI KHOẢN", "Mã xác nhận tài khoản của bạn là: " + randomCode + "\nMã sẽ hết hạn sau 2 phút.");
        pendingConfirmation.add(AccountConfirmation.builder()
                .email(email)
                .confirmationCode(randomCode)
                .expiration(LocalDateTime.now().plusMinutes(2L)).build());
        System.out.println("-------------------------------------------------");
        System.out.println("Added a new AccountConfirmation to list: ");
        pendingConfirmation.forEach(accountConfirmation -> System.out.println(accountConfirmation.getEmail()));
    }
}
