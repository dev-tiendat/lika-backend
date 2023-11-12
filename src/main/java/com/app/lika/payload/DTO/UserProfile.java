package com.app.lika.payload.DTO;

import com.app.lika.model.role.Role;
import com.app.lika.model.user.Gender;
import com.app.lika.model.user.Status;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserProfile {

    private Long id;

    private String username;

    private String email;

    private String phoneNumber = "";

    private String firstName;

    private String lastName;

    private String address;

    private Gender gender;

    private Date dateOfBirth;

    private Status status;

    private List<Role> roles;
}
