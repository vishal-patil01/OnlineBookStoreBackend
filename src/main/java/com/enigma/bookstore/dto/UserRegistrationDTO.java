package com.enigma.bookstore.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserRegistrationDTO {
    @NotNull
    @Pattern(regexp = "^.{3,50}$",message = "Minimum 3 characters Required")
    public String fullName;

    @NotNull
    @Pattern(regexp = "^([a-zA-Z]{3,}([.|_|+|-]?[a-zA-Z0-9]+)?[@][a-zA-Z0-9]+[.][a-zA-Z]{2,3}([.]?[a-zA-Z]{2,3})?)$",message = "Invalid Email Address")
    public String email;

    @NotNull
    @Pattern(regexp = "^((?=[^@|#|&|%|$]*[@|&|#|%|$][^@|#|&|%|$]*$)*(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9#@$?]{8,})$",message = "Password Must have At least *characters And Contains 1 Uppercase 1 Lowercase 1 Special Character 1 Digit")
    public String password;

    @NotNull
    @Pattern(regexp ="^([6-9][0-9]{9})$", message = "Invalid Mobile Number")
    public String phoneNo;

    public boolean emailVerificationStatus;

    public UserRegistrationDTO(String fullName, String email, String password, String phoneNo, boolean status) {
        this.fullName = fullName;
        this.email = email.toLowerCase().trim();
        this.password = password;
        this.phoneNo = phoneNo;
        this.emailVerificationStatus = status;
    }
}
