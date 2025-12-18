package com.farukgenc.boilerplate.springboot.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectDetailDto {

    private String associatedProject;

    private String projectPhase;

    private Long newServiceLineId;

}
