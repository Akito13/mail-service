package com.example.bookshop.mailservice.dto;


import com.example.bookshop.mailservice.model.AccountConfirmation;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationDto {
    @NotBlank(message = "Email không để trống")
    private String email;
    @NotBlank(message = "Chưa nhập mã xác nhận")
    private String confirmationCode;

    @Override
    public boolean equals(Object obj) {
        AccountConfirmation other = (AccountConfirmation) obj;
        System.out.println("------------------------------------------");
        System.out.println("INSIDE AccountConfirmation equals()\n");
        System.out.println("Email from request: " + other.getEmail());
        System.out.println("Saved email: " + this.email);
        return other.getEmail().equals(this.email);
    }
}
