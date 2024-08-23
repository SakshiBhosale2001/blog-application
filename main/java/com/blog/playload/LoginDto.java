package com.blog.playload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginDto {

    @NotEmpty(message = "Email should not be null !!")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
            ,message = "Invalid email address")
    private String email;

    @NotEmpty(message = "Password must not be Null !!")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,20}$"
            ,message = "Password must be between 8 and 20 characters long," +
            "contain at least one digit, one uppercase letter," +
            " one lowercase letter, and one special character. No whitespace allowed.")
    private String password;
}
