package com.example.bookshop.mailservice.model;

import com.example.bookshop.mailservice.dto.ConfirmationDto;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountConfirmation {
    private String email;
    private String confirmationCode;
    private LocalDateTime expiration;
}
