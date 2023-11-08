package com.example.bookshop.mailservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailInfo {
    String to;
    String[] cc;
    String[] bcc;
    String subject;
    String body;
}