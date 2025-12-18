package com.farukgenc.boilerplate.springboot.security.dto;

import lombok.Data;

@Data
public class ForUserRoleResponse<T> {

    private String message;

    private T response;

}
