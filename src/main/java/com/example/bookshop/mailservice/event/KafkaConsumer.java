package com.example.bookshop.mailservice.event;

import com.example.bookshop.mailservice.dto.ConfirmationDto;
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
        addConfirmationInfo(email, randomCode, null);
        pendingConfirmation.forEach(accountConfirmation1 -> System.out.println(accountConfirmation1.getEmail()));
    }

    @KafkaListener(
            topics = "${constant.kafka.account-password-change}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePasswordChangeEvent(ConsumerRecord<String, String> record) throws MessagingException {
        String[] info = record.value().split(":");
        String email = info[0];
        String password = info[1];
        String randomCode = Randomizer.random();
        mailService.send(email, "MÃ XÁC NHẬN MẬT KHẨU", "Mã xác nhận của bạn là: " + randomCode);
        addConfirmationInfo(email, randomCode, password);
    }

    private void addConfirmationInfo(String email, String randomCode, String pwd) {
        int index = pendingConfirmation.indexOf(new ConfirmationDto(email, "", ""));
        AccountConfirmation accountConfirmation = AccountConfirmation.builder()
                .email(email)
                .confirmationCode(randomCode)
                .pwd(pwd)
                .expiration(LocalDateTime.now().plusMinutes(2L)).build();
        if(index >= 0) {
            pendingConfirmation.set(index, accountConfirmation);
        } else {
            pendingConfirmation.add(accountConfirmation);
        }
    }
}
