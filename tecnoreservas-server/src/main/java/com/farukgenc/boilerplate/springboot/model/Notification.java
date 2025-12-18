package com.farukgenc.boilerplate.springboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.farukgenc.boilerplate.springboot.model.enums.NotificationType;
import com.farukgenc.boilerplate.springboot.model.enums.NotificationStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId;

    @Column(length = 255)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    /**
     * Generates a personalized message based on the notification type.
     * 
     * @param senderName Name of the user who triggered the notification
     * @param projectName Name of the associated project
     * @return Personalized notification message
     */
    public String generateMessage(String senderName, String projectName) {
        if (this.reservation == null) {
            return "Notificación del sistema";
        }
        
        //Long reservationId = this.reservation.getId();
        
        return switch (this.notificationType) {
            case NEW_RESERVATION -> "Nueva solicitud de reserva " + /*reservationId +*/ " de " + senderName + " para el proyecto " + projectName;
            case ACCEPTED -> senderName + " ha aceptado tu reserva " +/*reservationId +*/" para el proyecto " + projectName;
            case REJECTED -> senderName + " ha rechazado tu reserva " +/*reservationId +*/" para el proyecto " + projectName;
        };
    }
}
