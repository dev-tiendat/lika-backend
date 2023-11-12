package com.app.lika.payload.request;

import com.app.lika.model.role.RoleName;
import com.app.lika.model.user.Gender;
import com.app.lika.model.user.Status;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class SignUpRequest {
    @NotBlank
    @Size(min = 6, max = 30)
    private String username;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String address;

    private Gender gender;

    private Date dateOfBirth;

    private Status status;

    private RoleName role;
}
