package com.farukgenc.boilerplate.springboot.security.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpertResponseDto {
    private Long id;
    private String name;
    private String username;
    private Long lineId;
    private String email;
}
