package com.farukgenc.boilerplate.springboot.security.dto.user;

import com.farukgenc.boilerplate.springboot.model.UserRole;
import com.farukgenc.boilerplate.springboot.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor // Constructor con todos los argumentos
@NoArgsConstructor
public class UserResponseDto {

    private Long id;

    private String name;

    private String lastname;

    private String username;

    private String email;

    private UserStatus userStatus;

    private UserRole userRole;

}
