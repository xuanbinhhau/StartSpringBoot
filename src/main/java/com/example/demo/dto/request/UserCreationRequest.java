package com.example.demo.dto.request;

import com.example.demo.entity.Role;
import com.example.demo.validatator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
     String username;
    @Size(min = 8,message = "PASSWORD_INVALID")
     String password;
     String firstname;
     String lastname;
     @DobConstraint(min = 18,message = "DATEOFBIRTH_INVALID")
     LocalDate dob;

}
