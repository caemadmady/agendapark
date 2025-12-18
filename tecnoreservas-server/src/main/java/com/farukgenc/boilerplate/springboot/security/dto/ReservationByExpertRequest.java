package com.farukgenc.boilerplate.springboot.security.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationByExpertRequest {
    private Long Talent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
