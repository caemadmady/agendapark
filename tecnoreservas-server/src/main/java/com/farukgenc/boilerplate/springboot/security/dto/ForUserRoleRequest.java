package com.farukgenc.boilerplate.springboot.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForUserRoleRequest {

    private Long idUser;

    private Long idServiceLine;

    private String projectName;

}
