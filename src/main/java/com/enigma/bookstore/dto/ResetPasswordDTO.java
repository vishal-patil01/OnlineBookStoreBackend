package com.enigma.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {
    @NotNull
    @Pattern(regexp = "^((?=[^@|#|&|%|$]*[@|&|#|%|$][^@|#|&|%|$]*$)*(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9#@$?]{8,})$", message = "Password Must have At least *characters And Contains 1 Uppercase 1 Lowercase 1 Special Character 1 Digit")
    public String password;
}
