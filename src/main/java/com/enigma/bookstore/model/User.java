package com.enigma.bookstore.model;


import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"email"})})
public class User implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native"
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
    @Pattern(regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$", message = "Invalid Email Address")
    private String email;
    @JsonIgnore

    private String password;

    private String phoneNo;

    @JsonIgnore
    private boolean emailVerified;

    @JsonIgnore
    private UserRole userRole;

    @JsonIgnore
    @CreationTimestamp
    private Timestamp registrationDate;

    public User(UserRegistrationDTO userRegistrationDTO) {
        this.fullName = userRegistrationDTO.fullName;
        this.email = userRegistrationDTO.email;
        this.password = userRegistrationDTO.password;
        this.phoneNo = userRegistrationDTO.phoneNo;
        this.emailVerified = userRegistrationDTO.emailVerificationStatus;
        this.userRole=userRegistrationDTO.userRole;
    }
}
