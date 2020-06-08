package com.enigma.bookstore.dto;

import com.enigma.bookstore.enums.UserRole;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
public class UserRegistrationDTO {
    @NotNull
    @Pattern(regexp = "^.{3,50}$", message = "Minimum 3 characters Required")
    public String fullName;

    @NotNull
    @Pattern(regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$", message = "Invalid Email Address")
    public String email;

    @NotNull
    @Pattern(regexp = "^((?=[^@|#|&|%|$]*[@|&|#|%|$][^@|#|&|%|$]*$)*(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9#@$?]{8,})$", message = "Password Must have At least *characters And Contains 1 Uppercase 1 Lowercase 1 Special Character 1 Digit")
    public String password;

    @NotNull
    @Pattern(regexp = "^([6-9][0-9]{9})$", message = "Invalid Mobile Number")
    public String phoneNo;

    public boolean emailVerificationStatus;

    public UserRole userRole;
}
