package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.model.*;
import com.farukgenc.boilerplate.springboot.repository.UserRepository;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationDto;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationResponse;
import com.farukgenc.boilerplate.springboot.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@CrossOrigin("*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRepository userRepository;

    @Operation(
            summary = "obtiene todas las reservas.",
            description = "devuelve la lista completa de todas las reservas sin importar el estado (cancelada, confirmada, cumplidad, etc.)",
            tags = "Reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "parametros o peticion mal realizada",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "acceso restringido",
                    content = @Content)
    })
    @GetMapping("/all")
    public ResponseEntity<List<ReservationDto>> getReservations(){
        return ResponseEntity.ok(reservationService.getReservations());
    }

    @Operation(
            summary = "obtiene todas las reservas filtradas por estado.",
            description = "devuelve la lista completa de todas las reservas filtradas por estado (cancelada, confirmada, cumplidad, etc).",
            tags = "Reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "parametros o peticion mal realizada",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "acceso restringido",
                    content = @Content)
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationDto>> getReservationsStatus(@PathVariable String status) {
        return ResponseEntity.ok(reservationService.getReservationByStatus(status));
    }

    @Operation(
            summary = "obtiene todas las reservas personales del usuario.",
            description = "devuelve la lista completa de las reservas del usuario en sesion.",
            tags = "Reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "parametros o peticion mal realizada",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "acceso restringido",
                    content = @Content)
    })
    @GetMapping("/user")
    public ResponseEntity<List<ReservationDto>> getReservationsByTalent() {
        return ResponseEntity.ok(reservationService.getReservationByUser());
    }

    @Operation(
            summary = "obtiene todas las reservas filtradas por linea de servicio.",
            description = "devuelve la lista completa de todas las reservas filtradas por linea de servicio (TICS, Ingenieria-diseño, biotecnologia-nanotecnologia y electronica-telecomunicaciones.",
            tags = "Reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "parametros o peticion mal realizada",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "acceso restringido",
                    content = @Content)
    })
    @GetMapping("/serviceline/{idServiceLine}")
    public ResponseEntity<List<ReservationDto>> getReservationsByServiceLine(@PathVariable Long idServiceLine){
        return ResponseEntity.ok(reservationService.getReservationByServiceLine(idServiceLine));
    }

    @Operation(
            summary = "obtiene todas las reservas filtradas por rangos de fecha.",
            description = "devuelve la lista completa de todas las reservas filtradas por rangos de fecha y hora (YYYY-MM-DD).",
            tags = "Reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "parametros o peticion mal realizada",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "acceso restringido",
                    content = @Content)
    })
    @GetMapping("/dates")
    public ResponseEntity<List<ReservationDto>> getReservationsByDates(@RequestParam String dateStart, @RequestParam String dateEnd){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(dateStart, formatter);
        LocalDateTime end = LocalDateTime.parse(dateEnd, formatter);

        return ResponseEntity.ok(reservationService.getReservationByDates(start, end));
    }

    @Operation(
            summary = "Modifica la reserva.",
            description = "Actualiza los datos de una reserva existente a partir de su identificador.",
            tags = "Reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva modificada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "parametros o peticion mal realizada",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "acceso restringido",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "reserva no encontrada",
                    content = @Content)
    })
    @PatchMapping("/modify/{id}")
    public ResponseEntity<String> modifyReservation(@Valid @PathVariable Long id, @RequestBody ReservationDto reservationDto){
        return ResponseEntity.ok(reservationService.modification(id, reservationDto));
    }

    @Operation(
            summary = "Cambia el estado de la reserva a cancelado.",
            tags = "Reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "reserva cancelada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "400", description = "parametros o peticion mal realizada",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "acceso restringido",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "reserva no encontrada",
                    content = @Content)
    })
    @PatchMapping("/canceled/{id}")
    public ResponseEntity<String> canceledReservation(@PathVariable Long id){
        return ResponseEntity.ok(reservationService.canceled(id));
    }

    @Operation(
            summary = "Cambia el estado de la reserva a confirmado.",
            tags = "Reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "reserva confirmada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "400", description = "parametros o peticion mal realizada",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "acceso restringido",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "reserva no encontrada",
                    content = @Content)
    })
    @PatchMapping("/confirmed/{id}")
    public ResponseEntity<String> confirmedReservation(@PathVariable Long id){
        return ResponseEntity.ok(reservationService.confirmed(id));
    }

    @Operation(
            summary = "Cambia el estado de la reserva a cumplida.",
            tags = "Reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "reserva cumplida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "400", description = "parametros o peticion mal realizada",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "acceso restringido",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "reserva no encontrada",
                    content = @Content)
    })
    @PatchMapping("/fulfilled/{id}")
    public ResponseEntity<String> fulfilledReservation(@PathVariable Long id){
        return ResponseEntity.ok(reservationService.fulfilled(id));
    }

    @Operation(
            summary = "Cambia el estado de la reserva a incumplida.",
            tags = "Reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "reserva incumplida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "400", description = "parametros o peticion mal realizada",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "acceso restringido",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "reserva no encontrada",
                    content = @Content)
    })
    @PatchMapping("/missed/{id}")
    public ResponseEntity<String> missedReservation(@PathVariable Long id){
        return ResponseEntity.ok(reservationService.missed(id));
    }


}
