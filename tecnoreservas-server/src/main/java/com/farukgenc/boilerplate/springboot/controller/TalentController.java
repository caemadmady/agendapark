package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.model.UserRole;
import com.farukgenc.boilerplate.springboot.security.dto.*;
import com.farukgenc.boilerplate.springboot.service.TalentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/talents")
@CrossOrigin("*")
public class TalentController {

    @Autowired
    private TalentService talentService;

    @GetMapping("/all")
    @Operation(
            summary = "Obtener todos los talentos",
            description = "Devuelve la lista completa de talentos registrados en el sistema.",
            tags = "Talent"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de talentos obtenida correctamente.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TalentDto.class)
            )
    )
    public ResponseEntity<List<TalentResponseDto>> getAll() {
        return ResponseEntity.ok(talentService.getTalents());
    }

    @GetMapping("/talent/session")
    @Operation(
            summary = "Muestra el talento en sesion",
            description = "Muestra el talento que sta en la sesion actual",
            tags = "Talent"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Talento en sesion retornado."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parametros o peticion mal realizada."
            )
    })
    public ResponseEntity<TalentResponseDto> getTalent(){
        return ResponseEntity.ok().body(talentService.getTalent());
    }

    @PostMapping("/create")
    @Operation(
            summary = "Crear nuevo talento",
            description = "Permite registrar un nuevo talento en el sistema enviando su información en el cuerpo de la solicitud.",
            tags = "Talent"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Talento creado exitosamente."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Los datos del talento son inválidos o incompletos."
            )
    })
    public ResponseEntity<String> createTalent(
            @Parameter(description = "Información del talento a crear.", required = true)
            @RequestBody TalentAndProjectDto talentAndProjectDto, Long newServiceLineId) {
        return ResponseEntity.ok(talentService.createTalent(talentAndProjectDto.getTalentDto(), talentAndProjectDto.getProjectDetailDto(), newServiceLineId));
    }

    @PatchMapping("/update/email/{id}")
    @Operation(
            summary = "Actualizar email",
            description = "Permite al talento cambiar o actualizar email.",
            tags = "Talent"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Correo actualizado exitosamente."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Los datos son inválidos o incompletos."
            )
    })
    public ResponseEntity<String> updateEmail(@PathVariable Long id, @RequestBody String email){
        return ResponseEntity.ok(talentService.updateEmail(id, email));
    }

    @PatchMapping("/change-password")
    @Operation(
            summary = "Cambiar contraseña",
            description = "Permite cambiar o actualizar la contraseña del talento.",
            tags = "Talent"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contraseña actualizada."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Los datos son inválidos o incompletos."
            )
    })
    public ResponseEntity<String> changePassword(@RequestBody String newPassword){
        return ResponseEntity.ok(talentService.changePassword(newPassword));
    }

    @PatchMapping("/active/{id}")
    @Operation(
            summary = "Asignar estado activo",
            description = "Permite cambiar el estado del talento a activo.",
            tags = "Talent"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado activo actualizado."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parametros o peticion mal realizada."
            )
    })
    public ResponseEntity<String> activeTalent(@PathVariable Long id){
        return ResponseEntity.ok(talentService.talentActive(id));
    }

    @PatchMapping("/inactive/{id}")
    @Operation(
            summary = "Asignar estado inactivo",
            description = "Permite cambiar el estado del talento a inactivo.",
            tags = "Talent"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado inactivo actualizado."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parametros o peticion mal realizada."
            )
    })
    public ResponseEntity<String> inactiveTalent(@PathVariable Long id){
        return ResponseEntity.ok(talentService.talentInactive(id));
    }

    @PatchMapping("/suspended/{id}")
    @Operation(
            summary = "Asignar estado suspendido",
            description = "Permite cambiar el estado del talento a suspendido.",
            tags = "Talent"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado suspendido actualizado."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parametros o peticion mal realizada."
            )
    })
    public ResponseEntity<String> suspendedTalent(@PathVariable Long id){
        return ResponseEntity.ok(talentService.talentSuspended(id));
    }

    /*@PostMapping("/create/reservation")
    @Operation(
            summary = "Crear reserva con rol de talento",
            description = "Crear reservas especificamente para el rol de talento",
            tags = "Talent"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva creada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La reserva no se pudo crear"
            )
    })
    public ResponseEntity<ReservationResponse> createReservationByTalent(@RequestBody ReservationRequest request){
        return ResponseEntity.ok().body(talentService.createReservation(request));
    }*/

    @PostMapping("/with-resources")
    @Operation(
            summary = "Crear reserva con rol de talento",
            description = "Crear reservas especificamente para el rol de talento y con recursos si son necesarios",
            tags = "Talent"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva creada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La reserva no se pudo crear"
            )
    })
    public ResponseEntity<ReservationResponse> createReservationWithResources(@RequestBody ReservationWithResourcesRequest reservationWithResourcesRequest){
        return ResponseEntity.ok().body(talentService.createReservationWithResources(reservationWithResourcesRequest));
    }

    @PatchMapping("/update/{idTalent}")
    public ResponseEntity<TalentResponseDto> updateTalent(@PathVariable Long idTalent, Long idProject , @RequestBody TalentDto talentDto){
        return ResponseEntity.ok().body(talentService.updateTalent(idTalent, idProject, talentDto));
    }
}
