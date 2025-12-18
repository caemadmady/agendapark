package com.farukgenc.boilerplate.springboot.security.mapper.notifications;

import com.farukgenc.boilerplate.springboot.model.Expert;
import com.farukgenc.boilerplate.springboot.model.Notification;
import com.farukgenc.boilerplate.springboot.model.Reservation;
import com.farukgenc.boilerplate.springboot.model.Talent;
import com.farukgenc.boilerplate.springboot.security.dto.notification.CreateNotificationRequest;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationDTO;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationPageDTO;
import org.springframework.data.domain.Page;

public class NotificationMapper {

    /**
     * Builds a CreateNotificationRequest from the provided parameters.
     */
    public static CreateNotificationRequest buildCreateNotificationRequest(Talent talent, Expert expert, Reservation reservation) {
        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setTalent(talent);
        request.setExpert(expert);
        request.setReservation(reservation);
        return request;
    }

    /**
     * Maps a Page of Notification entities to a NotificationPageDTO.
     */
    public static NotificationPageDTO mapPageToPagedResponse(Page<Notification> notificationPage) {
        NotificationPageDTO response = new NotificationPageDTO();
        response.setNotifications(notificationPage.map(NotificationMapper::mapEntityToDTO).getContent());
        response.setCurrentPage(notificationPage.getNumber() + 1); // Adjusted to 1-based index
        response.setTotalPages(notificationPage.getTotalPages());
        response.setTotalItems(notificationPage.getTotalElements());
        return response;
    }

    /**
     * Maps a Notification entity to a NotificationDTO.
     */
    public static NotificationDTO mapEntityToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setSenderId(notification.getSenderId());
        dto.setUserId(notification.getUser().getId());
        dto.setReservationId(notification.getReservation().getId());
        dto.setMessage(notification.getMessage());
        dto.setNotificationType(notification.getNotificationType());
        dto.setStatus(notification.getStatus());
        dto.setCreatedAt(notification.getCreatedAt().toString());
        dto.setSentAt(notification.getSentAt().toString());
        return dto;
    }
}
