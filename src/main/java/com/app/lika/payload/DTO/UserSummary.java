package com.app.lika.payload.DTO;

import com.app.lika.model.role.Role;
import lombok.Data;
import java.util.List;

@Data
public class UserSummary {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
}
