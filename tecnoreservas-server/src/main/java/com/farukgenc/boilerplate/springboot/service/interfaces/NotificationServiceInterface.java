package com.farukgenc.boilerplate.springboot.service.interfaces;

import com.farukgenc.boilerplate.springboot.model.Notification;
import com.farukgenc.boilerplate.springboot.model.Reservation;
import com.farukgenc.boilerplate.springboot.security.dto.notification.CreateNotificationRequest;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationDTO;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationPageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

import java.util.Optional;

public interface NotificationServiceInterface {

    NotificationPageDTO getNotifications(String status, Long userId, Pageable pageable);

    Flux<NotificationDTO> streamNotifications(Long userId);

    NotificationDTO createNotification(CreateNotificationRequest request, int flag);

    NotificationDTO updateNotificationStatusByReservation(Reservation reservation);

}
