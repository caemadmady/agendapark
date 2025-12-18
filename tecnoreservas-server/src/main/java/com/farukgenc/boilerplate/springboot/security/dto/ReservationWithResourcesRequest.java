package com.farukgenc.boilerplate.springboot.security.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationWithResourcesRequest {

    private Long projectId;

    @FutureOrPresent(message = "La fecha de inicio no puede ser menor a la actual")
    private LocalDateTime startDate;

    @FutureOrPresent(message = "La fecha de fin no puede ser menor a la actual")
    private LocalDateTime endDate;

    private Long talentId;

    private Long expertId;

    private List<Long> resourceIds;
}
