package com.farukgenc.boilerplate.springboot.security.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPageDTO {

    private List<NotificationDTO> notifications;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
