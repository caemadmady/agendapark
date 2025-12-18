package com.farukgenc.boilerplate.springboot.security.dto.notification;

import com.farukgenc.boilerplate.springboot.model.Expert;
import com.farukgenc.boilerplate.springboot.model.Reservation;
import com.farukgenc.boilerplate.springboot.model.Talent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNotificationRequest {

    private Talent talent;
    private Expert expert;
    private Reservation reservation;
}
