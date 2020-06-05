package com.enigma.bookstore.model;


import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer id;
    private String fullName;

    @NotNull
    @Column(unique = true, nullable = false)

    @NotNull
    @Pattern(regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$",message = "Invalid Email Address")
    private String email;
    @JsonIgnore

    private String password;

    private String phoneNo;

    @JsonIgnore
    private boolean emailVerified;

    @JsonIgnore
    @CreationTimestamp
    private Timestamp registrationDate;

    public User(UserRegistrationDTO userRegistrationDTO) {
        this.fullName = userRegistrationDTO.fullName;
        this.email = userRegistrationDTO.email;
        this.password = userRegistrationDTO.password;
        this.phoneNo = userRegistrationDTO.phoneNo;
        this.emailVerified = userRegistrationDTO.emailVerificationStatus;
    }
}
