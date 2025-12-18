package com.farukgenc.boilerplate.springboot.security.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TalentResponseDto {
    private Long id;
    private String name;
    private String username;
    private List<Long> lineProjectId;
    private String email;
}
