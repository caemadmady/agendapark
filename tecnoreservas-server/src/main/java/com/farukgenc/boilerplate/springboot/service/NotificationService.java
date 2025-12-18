package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.Notification;
import com.farukgenc.boilerplate.springboot.model.Reservation;
import com.farukgenc.boilerplate.springboot.model.User;
import com.farukgenc.boilerplate.springboot.model.enums.NotificationStatus;
import com.farukgenc.boilerplate.springboot.model.enums.NotificationType;
import com.farukgenc.boilerplate.springboot.repository.NotificationRepository;
import com.farukgenc.boilerplate.springboot.repository.ReservationRepository;
import com.farukgenc.boilerplate.springboot.repository.UserRepository;
import com.farukgenc.boilerplate.springboot.security.dto.notification.CreateNotificationRequest;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationDTO;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationPageDTO;
import com.farukgenc.boilerplate.springboot.security.mapper.notifications.NotificationMapper;
import com.farukgenc.boilerplate.springboot.service.interfaces.NotificationServiceInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService implements NotificationServiceInterface {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final Sinks.Many<NotificationDTO> notificationSink = Sinks.many().multicast().onBackpressureBuffer();

    public NotificationService(NotificationRepository notificationRepository, 
                               UserRepository userRepository,
                               ReservationRepository reservationRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public NotificationPageDTO getNotifications(String status, Long userId, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findAll(pageable);

        // Convert Page to a List for filtering
        List<Notification> filteredNotifications = notifications.getContent().stream()
            .filter(notification -> status == null || notification.getStatus().name().equalsIgnoreCase(status))
            .filter(notification -> userId == null || notification.getUser().getId().equals(userId))
            .toList();

        // Map the filtered list back to a Page
        Page<Notification> filteredPage = new PageImpl<>(filteredNotifications, pageable, filteredNotifications.size());

        return NotificationMapper.mapPageToPagedResponse(filteredPage);
    }

    @Override
    public Flux<NotificationDTO> streamNotifications(Long userId) {
        // 1. Emitir notificaciones pendientes/históricas al conectar
        List<NotificationDTO> pending = notificationRepository.findAll().stream()
            .filter(n -> n.getUser().getId().equals(userId))
            .filter(n -> n.getStatus().name().equalsIgnoreCase("PENDING"))
            .map(NotificationMapper::mapEntityToDTO)
            .toList();
        Flux<NotificationDTO> pendingFlux = Flux.fromIterable(pending);

        // 2. Emitir nuevas notificaciones en tiempo real
        Flux<NotificationDTO> realtimeFlux = notificationSink.asFlux()
            .filter(dto -> dto.getUserId().equals(userId));

        // 3. Combinar ambos flujos
        return Flux.concat(pendingFlux, realtimeFlux);
    }

    @Override
    public NotificationDTO createNotification(CreateNotificationRequest request, int flag) {
        // 1. Asignar las entidades relacionadas
        User talent = request.getTalent();
        User expert = request.getExpert();
        Reservation reservation = request.getReservation();
        
        // 2. Crear la entidad Notification
        Notification notification = new Notification();
        notification.setReservation(reservation);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSentAt(LocalDateTime.now());
        //Notificacion creada del lado del Experto
        if (flag == 0){
            notification.setSenderId(expert.getId());
            notification.setUser(talent);
            notification.setNotificationType(NotificationType.ACCEPTED);
            notification.setStatus(NotificationStatus.PENDING);
            // 3. Generar y asignar el mensaje personalizado
            String senderName = expert.getName() + " " + expert.getLastname();
            String projectName = reservation.getTalent().getTalentProjectDetails().getFirst().getAssociatedProject();
            notification.setMessage(notification.generateMessage(senderName, projectName));
        }
        //Notificacion creada del lado del Talento
        if (flag == 1){
            notification.setSenderId(talent.getId());
            notification.setUser(expert);
            notification.setNotificationType(NotificationType.NEW_RESERVATION);
            notification.setStatus(NotificationStatus.PENDING);
            // 3. Generar y asignar el mensaje personalizado
            String senderName = talent.getName() + " " + talent.getLastname();
            String projectName = reservation.getTalent().getTalentProjectDetails().getFirst().getAssociatedProject();
            notification.setMessage(notification.generateMessage(senderName, projectName));
        }
        
        // 3. Generar y asignar el mensaje personalizado
        //String senderName = talent.getName() + " " + talent.getLastname();
        //String projectName = reservation.getTalent().getTalentProjectDetails().getFirst().getAssociatedProject();
        //notification.setMessage(notification.generateMessage(senderName, projectName));
        
        // 4. Guardar en la base de datos
        Notification savedNotification = notificationRepository.save(notification);
        
        // 5. Convertir a DTO
        NotificationDTO notificationDTO = NotificationMapper.mapEntityToDTO(savedNotification);
        
        // 6. Publicar por SSE
        publishNotification(notificationDTO);
        
        // 7. Retornar el DTO
        return notificationDTO;
    }

    @Override
    public NotificationDTO updateNotificationStatusByReservation(Reservation reservation) {
        //1. Buscar la notificacion por el id de la reserva y el id del usuario
        Optional<Notification> notification =
                notificationRepository.findByReservationIdAndUserId(reservation.getId(),
                        reservation.getExpert().getId());
        //2. Asignar el recurso encontrado
        Notification updateNotification = notification.get();

        //3. Actualizar los estados de la notificacion
        updateNotification.setNotificationType(NotificationType.ACCEPTED);
        updateNotification.setStatus(NotificationStatus.VIEWED);
        Notification savedNotification = notificationRepository.save(updateNotification);

        //4. Convertir a DTO
        NotificationDTO notificationDTO = NotificationMapper.mapEntityToDTO(savedNotification);

        //5. Publicar por SSE
        publishNotification(notificationDTO);

        return notificationDTO;
    }

    // Método auxiliar para publicar nuevas notificaciones (llamar al crear una notificación)
    public void publishNotification(NotificationDTO notificationDTO) {
        notificationSink.tryEmitNext(notificationDTO);
    }
}
