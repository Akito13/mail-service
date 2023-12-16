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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Optional;
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
        int index = getValidAccountConfirmation(confirmationDto);
        pendingConfirmation.remove(index);
        ResponseDto response = ResponseDto.builder()
                .apiPath(request.getDescription(false))
                .message("Xác nhận thành công")
                .statusCode(HttpStatus.OK)
                .timestamp(LocalDateTime.now()).build();
        kafkaProducer.notifyAccountConfirmed(confirmationDto.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("pwd-change")
    public ResponseEntity checkPwdConfirmation(@RequestParam("email") Optional<String> email,
                                               @RequestParam("password") Optional<String> password,
                                               @RequestParam("confirmationCode") Optional<String> confirmationCode){
        if(email.isEmpty()) {
            throw new InvalidBodyException("Hãy cung cấp email");
        }
        if(password.isEmpty()) {
            throw new InvalidBodyException("Chưa điền mật khẩu");
        }
        if(confirmationCode.isEmpty()) {
            throw new InvalidBodyException("Thiếu mã xác nhận");
        }
        int index = getValidAccountConfirmation(new ConfirmationDto(email.get(), confirmationCode.get(), password.get()));
        pendingConfirmation.remove(index);
        kafkaProducer.notifyAccountPasswordConfirmed(email.get(), password.get());
        return ResponseEntity.ok().build();
    }

    private int getValidAccountConfirmation(ConfirmationDto confirmationDto) {
        int index = pendingConfirmation.indexOf(confirmationDto);
        if(index < 0) {
            throw new ConfirmationCodeExpiredException("Mã xác nhận đã hết hạn hoặc không tồn tại");
        };
        AccountConfirmation accountConfirmation = pendingConfirmation.get(index);
        if(!accountConfirmation.getConfirmationCode().equals(confirmationDto.getConfirmationCode())) {
            throw new InvalidBodyException("Mã xác nhận không đúng");
        }
        return index;
    }
}
