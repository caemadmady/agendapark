package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.*;
import com.farukgenc.boilerplate.springboot.model.enums.ReservationStatus;
import com.farukgenc.boilerplate.springboot.repository.*;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationWithResourcesRequest;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationDto;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationResponse;
import com.farukgenc.boilerplate.springboot.security.dto.notification.CreateNotificationRequest;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationDTO;
import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceResponse;
import com.farukgenc.boilerplate.springboot.security.mapper.notifications.NotificationMapper;
import com.farukgenc.boilerplate.springboot.security.service.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ReservationResourceService reservationResourceService;

    @Autowired
    private ReservationResourceRepository reservationResourceRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private TalentProjectDetailRepository talentProjectDetailRepository;

    public List<ReservationDto> getReservations() {
        List<Reservation> listaReservas = reservationRepository.findAll();
        List<ReservationDto> response = new ArrayList<>();
        for (Reservation reservation: listaReservas){
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setId(reservation.getId());
            reservationDto.setDateTimeStart(reservation.getDateTimeStart());
            reservationDto.setEndDateTime(reservation.getEndDateTime());
            reservationDto.setServiceLineId(reservation.getExpert().getServiceLine().getId());
            reservationDto.setExpert(reservation.getExpert().getId());
            reservationDto.setTalent(reservation.getTalent().getId());
            reservationDto.setNameExpert(reservation.getExpert().getName() +" "+ reservation.getExpert().getLastname() );
            reservationDto.setNameTalent(reservation.getTalent().getName() +" "+ reservation.getTalent().getLastname());
            if (reservation.getTalent() != null &&
                    reservation.getTalent().getTalentProjectDetails() != null) {
                reservationDto.setAssociateProject(
                        reservation.getTalent().getTalentProjectDetails().get(0).getAssociatedProject());
            }
            reservationDto.setStatus(reservation.getReservationStatus().toString());
            response.add(reservationDto);
        }
        return response;
    }

    public List<ReservationDto> getReservationByStatus(String status){
        ReservationStatus statusEnum = ReservationStatus.valueOf(status);
        List<Reservation> listStatus = reservationRepository.findAllByReservationStatus(statusEnum);
        List<ReservationDto> response = new ArrayList<>();
        for (Reservation reservation: listStatus) {
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setId(reservation.getId());
            reservationDto.setDateTimeStart(reservation.getDateTimeStart());
            reservationDto.setEndDateTime(reservation.getEndDateTime());
            reservationDto.setServiceLineId(reservation.getExpert().getServiceLine().getId());
            reservationDto.setExpert(reservation.getExpert().getId());
            reservationDto.setTalent(reservation.getTalent().getId());
            reservationDto.setNameExpert(reservation.getExpert().getName());
            reservationDto.setNameTalent(reservation.getTalent().getName());
            if (reservation.getTalent() != null &&
                    reservation.getTalent().getTalentProjectDetails() != null) {
                reservationDto.setAssociateProject(
                        reservation.getTalent().getTalentProjectDetails().get(0).getAssociatedProject());
            }
            reservationDto.setStatus(String.valueOf(ReservationStatus.valueOf(reservation.getReservationStatus().toString())));
            response.add(reservationDto);
        }
        return response;
    }

    public List<ReservationDto> getReservationByUser(){
        //Obtener el usuario logueado
        String username = userServiceImpl.getLoggedUser();
        //BUscar el usuario y guardarlo en variable
        User user = userRepository.findByUsername(username);
        List<ReservationDto> response = new ArrayList<>();
        if (user.getUserRole() == UserRole.EXPERT) {
            Expert expert = expertRepository.findById(user.getId()).orElseThrow();
            List<Reservation> listado = reservationRepository.findAllByExpert_Id(user.getId());
            for (Reservation reservation : listado) {
                    ReservationDto reservationDto = new ReservationDto();
                    reservationDto.setId(reservation.getId());
                    reservationDto.setDateTimeStart(reservation.getDateTimeStart());
                    reservationDto.setEndDateTime(reservation.getEndDateTime());
                    reservationDto.setServiceLineId(reservation.getExpert().getServiceLine().getId());
                    reservationDto.setExpert(reservation.getExpert().getId());
                    reservationDto.setTalent(reservation.getTalent().getId());
                    reservationDto.setNameExpert(reservation.getExpert().getName());
                    reservationDto.setNameTalent(reservation.getTalent().getName());
                    if (reservation.getTalent() != null &&
                        reservation.getTalent().getTalentProjectDetails() != null) {
                        reservationDto.setAssociateProject(
                                reservation.getTalent().getTalentProjectDetails().get(0).getAssociatedProject());
                    }
                    reservationDto.setStatus(reservation.getReservationStatus().toString());
                    response.add(reservationDto);
                }
                return response;
        } else if (user.getUserRole() == UserRole.TALENT) {
            Talent talent = talentRepository.findById(user.getId()).orElseThrow();
            List<Reservation> listado = reservationRepository.findAllByTalent_Id(user.getId());
            for (Reservation reservation : listado) {
                ReservationDto reservationDto = new ReservationDto();
                reservationDto.setId(reservationDto.getId());
                reservationDto.setDateTimeStart(reservation.getDateTimeStart());
                reservationDto.setEndDateTime(reservation.getEndDateTime());
                reservationDto.setServiceLineId(reservation.getExpert().getServiceLine().getId());
                reservationDto.setExpert(reservation.getExpert().getId());
                reservationDto.setTalent(reservation.getTalent().getId());
                reservationDto.setNameExpert(reservation.getExpert().getName());
                reservationDto.setNameTalent(reservation.getTalent().getName());
                if (reservation.getTalent() != null &&
                        reservation.getTalent().getTalentProjectDetails() != null) {
                    reservationDto.setAssociateProject(
                            reservation.getTalent().getTalentProjectDetails().get(0).getAssociatedProject());
                }
                reservationDto.setStatus(reservation.getReservationStatus().toString());
                response.add(reservationDto);
            }
        }
            return response;
    }

    public List<ReservationDto> getReservationByServiceLine(Long idServiceLine){

        List<Reservation> listServiceLine = reservationRepository.findAllByExpert_ServiceLine_Id(idServiceLine);
        System.out.println(listServiceLine);
        List<ReservationDto> response = new ArrayList<>();
        for (Reservation reservation: listServiceLine) {
            if (reservation.getExpert().getServiceLine().getId().equals(idServiceLine)){
                ReservationDto reservationDto = new ReservationDto();
                reservationDto.setId(reservation.getId());
                reservationDto.setDateTimeStart(reservation.getDateTimeStart());
                reservationDto.setEndDateTime(reservation.getEndDateTime());
                reservationDto.setServiceLineId(reservation.getExpert().getServiceLine().getId());
                reservationDto.setExpert(reservation.getExpert().getId());
                reservationDto.setTalent(reservation.getTalent().getId());
                reservationDto.setNameExpert(reservation.getExpert().getName());
                reservationDto.setNameTalent(reservation.getTalent().getName());
                if (reservation.getTalent() != null &&
                        reservation.getTalent().getTalentProjectDetails() != null) {
                    reservationDto.setAssociateProject(
                            reservation.getTalent().getTalentProjectDetails().get(0).getAssociatedProject());
                }
                reservationDto.setStatus(reservation.getReservationStatus().toString());
                response.add(reservationDto);
            }
        }
        return response;
    }

    public List<ReservationDto> getReservationByDates(LocalDateTime date1, LocalDateTime date2) {
        List<Reservation> listByDates = reservationRepository.findByDateTimeStartBetween(date1, date2);
        List<ReservationDto> response = new ArrayList<>();
        for (Reservation reservation : listByDates) {
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setId(reservation.getId());
            reservationDto.setDateTimeStart(reservation.getDateTimeStart());
            reservationDto.setEndDateTime(reservation.getEndDateTime());
            reservationDto.setServiceLineId(reservation.getExpert().getServiceLine().getId());
            reservationDto.setExpert(reservation.getExpert().getId());
            reservationDto.setTalent(reservation.getTalent().getId());
            reservationDto.setNameExpert(reservation.getExpert().getName());
            reservationDto.setNameTalent(reservation.getTalent().getName());
            if (reservation.getTalent() != null &&
                    reservation.getTalent().getTalentProjectDetails() != null) {
                reservationDto.setAssociateProject(
                        reservation.getTalent().getTalentProjectDetails().get(0).getAssociatedProject());
            }
            reservationDto.setStatus(reservation.getReservationStatus().toString());
            response.add(reservationDto);
        }
        return response;
    }

    @Transactional
    public ReservationResponse createReservation(ReservationDto reservationDto, UserRole userRole,int flag) {
        //Validacion del experto y talento
        Long expertId = reservationDto.getExpert();
        Optional<Expert> expert = expertRepository.findById(expertId);
        Long talentId = reservationDto.getTalent();
        Optional<Talent> talent = talentRepository.findById(talentId);
        //asignacion de tiempo
        LocalDateTime start = reservationDto.getDateTimeStart();
        LocalDateTime end = reservationDto.getEndDateTime();
        LocalTime time = start
                .atZone(ZoneId.systemDefault())
                .toLocalTime();
        // horario no disponible
        LocalTime almuerzoInicio = LocalTime.of(12,0);
        LocalTime almuerzoFin = LocalTime.of(14,0);
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();
        LocalDate fecha1 = start.toLocalDate();
        LocalDate fecha2 = end.toLocalDate();
        LocalTime inicio = LocalTime.of(8,0);
        LocalTime fin = LocalTime.of(16,0);
        LocalDate hoy = LocalDate.now();
        boolean intersectaConAlmuerzo = !endTime.isBefore(almuerzoInicio) && !startTime.isAfter(almuerzoFin);

        if (time.isBefore(inicio) || time.isAfter(fin)) {
            throw new IllegalArgumentException("solo se permiten reservas entre las 8:00 y 16:00 horas.");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("El tiempo de fin de la reserva no puede ser menor al tiempo de inicio de la reserva.");
        }
        if (end.getHour() - start.getHour() < 1) {
            throw new IllegalArgumentException("El tiempo de reserva no puede ser inferior a una hora.");
        }
        if (fecha1.isBefore(hoy)) {
            throw new IllegalArgumentException("Solo se puede reservar hoy o en los siguientes dias.");
        }
        if (!fecha1.atStartOfDay().equals(fecha2.atStartOfDay())) {
            throw new IllegalArgumentException("Las fechas de inicio y fin de la reserva deben ser en el mismo día.");
        }
        if (intersectaConAlmuerzo) {
            throw new IllegalArgumentException("No se puede reservar entre las 12:01 y las 13:59.");
        }
        //obtencion de datos
        Reservation newReservation = new Reservation();
        newReservation.setDateTimeStart(reservationDto.getDateTimeStart());
        newReservation.setEndDateTime(reservationDto.getEndDateTime());
        if (userRole.equals(UserRole.EXPERT)){
            newReservation.setReservationStatus(ReservationStatus.CONFIRMADA);
        } else if (userRole.equals(UserRole.TALENT)) {
            newReservation.setReservationStatus(ReservationStatus.SOLICITADA);
        }
        newReservation.setCreationDate(LocalDateTime.now());
        newReservation.setLastModifiedDate(LocalDateTime.now());
        newReservation.setExpert(expert.get());
        newReservation.setTalent(talent.get());
        // Crear nueva notificacion
        Reservation reservation = reservationRepository.save(newReservation);
        CreateNotificationRequest notificationRequestDto =
                NotificationMapper.buildCreateNotificationRequest(talent.get(),expert.get(),reservation);
        NotificationDTO notificationDTO = notificationService.createNotification(notificationRequestDto, flag);
        //Fin del registro de una notificación

        Optional<User> userTalento = userRepository.findById(talentId);
        ReservationResponse reservationResponse = new ReservationResponse();
        reservationResponse.setDateTimeStart(reservation.getDateTimeStart());
        reservationResponse.setEndDateTime(reservation.getEndDateTime());
        reservationResponse.setStatus(reservation.getReservationStatus());
        reservationResponse.setServiceLine(reservation.getExpert().getServiceLine().getId().toString());
        reservationResponse.setExpert(reservation.getExpert().getId().toString());
        reservationResponse.setTalent(reservation.getTalent().getId().toString());
        return reservationResponse;
    }

    public String modification(Long id, ReservationDto reservationDto) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        //valida que la fecha no venga vacía
        LocalDateTime start = reservationDto.getDateTimeStart();
        LocalDateTime end = reservationDto.getEndDateTime();
        LocalTime time = start
                .atZone(ZoneId.systemDefault())
                .toLocalTime();
        //limitacion de horas
        LocalTime inicio = LocalTime.of(8,0);
        LocalTime almuerzoInicio = LocalTime.of(12, 1);
        LocalTime almuerzoFin = LocalTime.of(13,59);
        LocalTime fin = LocalTime.of(16,0);
        LocalDate fecha1 = start.toLocalDate();
        LocalDate fecha2 = end.toLocalDate();
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();
        boolean intersectaConAlmuerzo = !endTime.isBefore(almuerzoInicio) && !startTime.isAfter(almuerzoFin);

        if (time.isBefore(inicio) || time.isAfter(fin)) {
            throw new IllegalArgumentException("Solo se permiten reservas entre las 8:00 y 16:00 horas.");
        }
        if (endTime.isAfter(fin)) {
            throw new IllegalArgumentException("La hora de fin no puede ser despues de las 16:00");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("El tiempo de fin de la reserva no puede ser menor al tiempo de inicio de la reserva.");
        }
        if (end.getHour() - start.getHour() < 1) {
            throw new IllegalArgumentException("El tiempo de reserva no puede ser inferior a una hora.");
        }
        if (!fecha1.equals(fecha2)) {
            throw new IllegalArgumentException("Las fechas de inicio y fin de la reserva deben ser en el mismo día.");
        }
        if (intersectaConAlmuerzo) {
            throw new IllegalArgumentException("No se puede reservar entre las 12:01 y las 13:59.");
        }

        reservation.setDateTimeStart(reservationDto.getDateTimeStart());
        reservation.setEndDateTime(reservationDto.getEndDateTime());
        reservation.setLastModifiedDate(LocalDateTime.now());

        reservationRepository.save(reservation);
        return "la reserva de " + reservation.getTalent().getName() + " ha sido modificada.";
    }

    @Transactional
    public String canceled(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));

        reservation.setReservationStatus(ReservationStatus.CANCELADA);
        reservationRepository.save(reservation);

        return "La reserva de " + reservation.getTalent().getName() + " ha sido cancelada.";
    }

    @Transactional
    public String confirmed(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));

        reservation.setReservationStatus(ReservationStatus.CONFIRMADA);
        Reservation reservationResponse = reservationRepository.save(reservation);

        //Cambiar el estado de la notifcación de PENDING a VIEWED
        NotificationDTO notificationDTO =notificationService.updateNotificationStatusByReservation(reservationResponse);

        return "La reserva de " + reservation.getTalent().getName() + " ha sido confirmada.";
    }

    @Transactional
    public String fulfilled(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));

        reservation.setReservationStatus(ReservationStatus.CUMPLIDA);
        reservationRepository.save(reservation);

        return "La reserva de " + reservation.getTalent().getName() + " ha sido cumplida.";
    }

    @Transactional
    public String missed(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));

        reservation.setReservationStatus(ReservationStatus.INCUMPLIDA);
        reservationRepository.save(reservation);

        return "La reserva de " + reservation.getTalent().getName() + " ha sido incumplida.";
    }

    @Transactional
    public ReservationResponse createReservationWithResource(ReservationWithResourcesRequest request) {

        // Obtener el usuario autenticado
        String username = userServiceImpl.getLoggedUser();
        User user = userRepository.findByUsername(username);
        UserRole userRole = user.getUserRole();

        LocalDateTime start = request.getStartDate();
        LocalDateTime end = request.getEndDate();
        LocalTime time = start
                .atZone(ZoneId.systemDefault())
                .toLocalTime();
        // horario no disponible
        LocalTime almuerzoInicio = LocalTime.of(12,0);
        LocalTime almuerzoFin = LocalTime.of(14,0);
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();
        LocalDate fecha1 = start.toLocalDate();
        LocalDate fecha2 = end.toLocalDate();
        LocalTime inicio = LocalTime.of(8,0);
        LocalTime fin = LocalTime.of(16,0);
        LocalDate hoy = LocalDate.now();
        boolean intersectaConAlmuerzo = !endTime.isBefore(almuerzoInicio) && !startTime.isAfter(almuerzoFin);

        if (time.isBefore(inicio) || time.isAfter(fin)) {
            throw new IllegalArgumentException("solo se permiten reservas entre las 8:00 y 16:00 horas.");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("El tiempo de fin de la reserva no puede ser menor al tiempo de inicio de la reserva.");
        }
        if (end.getHour() - start.getHour() < 1) {
            throw new IllegalArgumentException("El tiempo de reserva no puede ser inferior a una hora.");
        }
        if (fecha1.isBefore(hoy)) {
            throw new IllegalArgumentException("Solo se puede reservar hoy o en los siguientes dias.");
        }
        if (!fecha1.atStartOfDay().equals(fecha2.atStartOfDay())) {
            throw new IllegalArgumentException("Las fechas de inicio y fin de la reserva deben ser en el mismo día.");
        }
        if (intersectaConAlmuerzo) {
            throw new IllegalArgumentException("No se puede reservar entre las 12:01 y las 13:59.");
        }

        // Validar experto y talento
        Expert expert = expertRepository.findById(request.getExpertId())
                .orElseThrow(() -> new RuntimeException("Expert not found " + request.getExpertId()));

        Talent talent = talentRepository.findById(request.getTalentId())
                .orElseThrow(() -> new RuntimeException("Talent not found " + request.getTalentId()));


        // Crear reserva base
        Reservation reservation = new Reservation();
        reservation.setExpert(expert);
        reservation.setTalent(talent);
        reservation.setDateTimeStart(request.getStartDate());
        reservation.setEndDateTime(request.getEndDate());
        reservation.setCreationDate(LocalDateTime.now());
        reservation.setLastModifiedDate(LocalDateTime.now());

        // Estado según rol
        if (userRole == UserRole.EXPERT) {
            reservation.setReservationStatus(ReservationStatus.CONFIRMADA);
        } else {
            reservation.setReservationStatus(ReservationStatus.SOLICITADA);
        }

        // Guardar reserva
        reservation = reservationRepository.save(reservation);
        // Crear nueva notificacion
        int flag = 1;
        CreateNotificationRequest notificationRequestDto =
                NotificationMapper.buildCreateNotificationRequest(talent,expert,reservation);
        NotificationDTO notificationDTO = notificationService.createNotification(notificationRequestDto, flag);

        // Asignar recursos usando tu servicio actual
        if (request.getResourceIds() != null && !request.getResourceIds().isEmpty()) {
            reservationResourceService.assignResource(request.getResourceIds(), reservation.getId());
        }

        // Respuesta
        ReservationResponse response = new ReservationResponse();
        response.setDateTimeStart(reservation.getDateTimeStart());
        response.setEndDateTime(reservation.getEndDateTime());
        response.setStatus(reservation.getReservationStatus());
        response.setServiceLine(expert.getServiceLine().getServiceLineName());
        response.setExpert(expert.getName() + " " + expert.getLastname());
        response.setTalent(talent.getName() + " " + talent.getLastname());

        List<ReservationResource> reservationResources = reservationResourceRepository.findByReservation_Id(reservation.getId());

        List<CreateResourceResponse> resourceResponses = new ArrayList<>();

        for (ReservationResource reservationId: reservationResources) {
            Resource item = resourceRepository.findById(reservationId.getResource().getId()).orElseThrow();
            if (item.getServiceLine().getId().equals(expert.getServiceLine().getId())) {
                CreateResourceResponse createResourceResponse = new CreateResourceResponse();
                createResourceResponse.setId(item.getId());
                createResourceResponse.setName(item.getName());
                resourceResponses.add(createResourceResponse);
            } else {
                System.out.println("El recurso no pertenece a la linea de servicio en la que va a reservar");
            }
        }

        if (request.getProjectId() != null) {
            TalentProjectDetail talentProjectDetail = talentProjectDetailRepository.findById(request.getProjectId()).orElseThrow();
            if (talentProjectDetail.getTalent().getId().equals(talent.getId()) && talentProjectDetail.getServiceLine().getId().equals(expert.getServiceLine().getId())){
                response.setProjectId(request.getProjectId());
                response.setProjectName(talentProjectDetail.getAssociatedProject());
            } else {
                throw new IllegalArgumentException("El proyecto no pertenece al talento en sesion o no pertenece a la linea de servicio en el que esta registrado");
            }
        }

        response.setResourcesId(resourceResponses);

        return response;
    }


}
