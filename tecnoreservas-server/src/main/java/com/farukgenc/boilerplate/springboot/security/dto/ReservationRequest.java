package com.farukgenc.boilerplate.springboot.security.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequest {

    private Long serviceLine;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
