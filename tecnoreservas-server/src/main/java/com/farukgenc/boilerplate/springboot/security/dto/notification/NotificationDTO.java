package com.farukgenc.boilerplate.springboot.security.dto.notification;

import com.farukgenc.boilerplate.springboot.model.enums.NotificationStatus;
import com.farukgenc.boilerplate.springboot.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private Long id;
    private Long senderId;
    private String message;
    private NotificationType notificationType;
    private NotificationStatus status;
    private String createdAt;
    private String sentAt;
    private Long userId;
    private Long reservationId;
}
