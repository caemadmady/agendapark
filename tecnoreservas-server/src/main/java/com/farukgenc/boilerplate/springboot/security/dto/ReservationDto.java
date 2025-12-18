package com.farukgenc.boilerplate.springboot.security.dto;

import com.farukgenc.boilerplate.springboot.model.enums.ReservationStatus;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationDto {

    private Long id;

    @FutureOrPresent(message = "La fecha de inicio no puede ser menor a la actual")
    private LocalDateTime dateTimeStart;
    @FutureOrPresent(message = "La fecha de fin no puede ser menor a la actual")
    private LocalDateTime endDateTime;

    private Long expert;

    private Long talent;

    private String status;

    private String nameTalent;

    private String nameExpert;

    private Long serviceLineId;

    private String associateProject;

}
