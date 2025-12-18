package com.farukgenc.boilerplate.springboot.security.dto;

import com.farukgenc.boilerplate.springboot.model.enums.ReservationStatus;
import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceResponse;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReservationResponse {

    private LocalDateTime dateTimeStart;

    private LocalDateTime endDateTime;

    private ReservationStatus status;

    private String serviceLine;

    private String expert;

    private String talent;

    private List<CreateResourceResponse> resourcesId;

    private Long projectId;

    private String projectName;

}
