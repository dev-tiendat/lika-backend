package com.app.lika.payload.request;

import com.app.lika.model.role.RoleName;
import com.app.lika.model.user.Gender;

import jakarta.validation.constraints.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
public class UserRequest {
    @NotBlank
    @Size(min = 6, max = 30)
    private String username;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

    @NotBlank
    @Email
    private String email;

    private String phoneNumber;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String address;

    @NotNull
    private Gender gender;

    @NotNull
    private Date dateOfBirth;

    @NotEmpty
    private List<@NotNull RoleName> roles;

    public List<RoleName> getRoles() {
        return roles == null ? null : new ArrayList<>(roles);
    }

    public void setRoles(List<RoleName> roles) {
        if (roles == null) {
            this.roles = null;
        } else {
            this.roles = Collections.unmodifiableList(roles);
        }
    }

}
