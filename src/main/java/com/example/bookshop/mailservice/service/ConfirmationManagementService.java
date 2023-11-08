package com.example.bookshop.mailservice.service;

import com.example.bookshop.mailservice.model.AccountConfirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@EnableScheduling
public class ConfirmationManagementService {

    private CopyOnWriteArrayList<AccountConfirmation> pendingConfirmation;

    @Autowired
    public ConfirmationManagementService(CopyOnWriteArrayList<AccountConfirmation> pendingConfirmation) {
        this.pendingConfirmation = pendingConfirmation;
    }

    @Scheduled(fixedRate = 1000)
    public void checkConfirmationExpiration() {
        if(Objects.isNull(pendingConfirmation) || pendingConfirmation.isEmpty()){
            return;
        }
        AccountConfirmation confirmation = pendingConfirmation.get(0);
        if(LocalDateTime.now().isAfter(confirmation.getExpiration())){
            pendingConfirmation.remove(0);
            System.out.println("Removed ");
        }

    }
}
