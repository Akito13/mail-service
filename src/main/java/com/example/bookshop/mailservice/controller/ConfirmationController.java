package com.example.bookshop.mailservice.controller;

import com.example.bookshop.mailservice.dto.ConfirmationDto;
import com.example.bookshop.mailservice.dto.ResponseDto;
import com.example.bookshop.mailservice.event.KafkaProducer;
import com.example.bookshop.mailservice.exception.ConfirmationCodeExpiredException;
import com.example.bookshop.mailservice.exception.InvalidBodyException;
import com.example.bookshop.mailservice.model.AccountConfirmation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("api/confirm")
public class ConfirmationController {

    private CopyOnWriteArrayList<AccountConfirmation> pendingConfirmation;
    private KafkaProducer kafkaProducer;

    @Autowired
    public ConfirmationController(CopyOnWriteArrayList<AccountConfirmation> pendingConfirmation,
                                  KafkaProducer kafkaProducer) {
        this.pendingConfirmation = pendingConfirmation;
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping(value = "account-register")
    public ResponseEntity<ResponseDto> checkConfirmationCode(@Valid  @RequestBody ConfirmationDto confirmationDto,
                                                             WebRequest request) {
        System.out.println("---------------------------------------");
        System.out.println("Inside Controller, info from request: " + confirmationDto.getEmail() + " -> " + confirmationDto.getConfirmationCode());
        int index = pendingConfirmation.indexOf(confirmationDto);
        if(index < 0) {
            throw new ConfirmationCodeExpiredException("Mã xác nhận đã hết hạn hoặc không tồn tại");
        };
        AccountConfirmation accountConfirmation = pendingConfirmation.get(index);
        if(!accountConfirmation.getConfirmationCode().equals(confirmationDto.getConfirmationCode())) {
            throw new InvalidBodyException("Mã xác nhận không đúng");
        }
        pendingConfirmation.remove(index);
        ResponseDto response = ResponseDto.builder()
                .apiPath(request.getDescription(false))
                .message("Xác nhận thành công")
                .statusCode(HttpStatus.OK)
                .timestamp(LocalDateTime.now()).build();
        kafkaProducer.notifyAccountConfirmed(accountConfirmation.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
