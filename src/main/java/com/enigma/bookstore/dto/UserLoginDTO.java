package com.enigma.bookstore.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserLoginDTO {

    @NotNull
    @Pattern(regexp = "^([a-zA-Z]{3,}([.|_|+|-]?[a-zA-Z0-9]+)?[@][a-zA-Z0-9]+[.][a-zA-Z]{2,3}([.]?[a-zA-Z]{2,3})?)$",message = "Invalid Email Address")
    @Column(unique = true,nullable = false)
    public String email;

    @NotNull
    @Pattern(regexp = "^((?=[^@|#|&|%|$]*[@|&|#|%|$][^@|#|&|%|$]*$)*(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9#@$?]{8,})$",message = "Password Must have At least *characters And Contains 1 Uppercase 1 Lowercase 1 Special Character 1 Digit")
    public String password;

    public UserLoginDTO(String email, String password) {
        this.email = email.toLowerCase().trim();
        this.password = password;
    }
}
