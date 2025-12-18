package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.model.UserRole;
import com.farukgenc.boilerplate.springboot.security.dto.*;
import com.farukgenc.boilerplate.springboot.service.ExpertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experts")
@CrossOrigin("*")
public class ExpertController {

    @Autowired
    private ExpertService expertService;

    @PostMapping("/create-expert")
    @Operation(
            summary = "Registra expertos",
            description = "Permite registrar expertos en el sistema.",
            tags = "Expert"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Experto registrado."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parametros o peticion mal realizada."
            )
    })
    public ResponseEntity<String> createExpert(@RequestBody ExpertDto expertDto){
        return ResponseEntity.ok(expertService.createExpert(expertDto));
    }

    /*@PostMapping("/create-reservations")
    @Operation(
            summary = "Crea una reserva por experto",
            description = "Crea una reserva especificamente para el rol",
            tags = "Expert"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva creada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error al crear la reserva"
            )
    })
    public ResponseEntity<ReservationResponse> createReservations(@RequestBody ReservationByExpertRequest request){
        return ResponseEntity.ok(expertService.createReservationsByExpert(request));
    }*/

    @GetMapping("/all")
    @Operation(
            summary = "Lista de todos los expertos",
            description = "Permite la vista de la lista de todos los expertos",
            tags = "Expert"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de expertos."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parametros o peticion mal realizada."
            )
    })
    public ResponseEntity<List<ExpertResponseDto>> getAllExperts(){
        return ResponseEntity.ok(expertService.getAllExperts());
    }

    @GetMapping("/expert/session")
    @Operation(
            summary = "Muestra el experto en sesion",
            description = "Muestra el experto en sesion",
            tags = "Expert"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Experto en sesion retornado."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parametros o peticion mal realizada."
            )
    })
    public ResponseEntity<ExpertResponseDto> getExpert(){
        return ResponseEntity.ok().body(expertService.getExpert());
    }

    @PatchMapping("/update/email/{id}")
    @Operation(
            summary = "Actualizar email",
            description = "Permite al experto cambiar o actualizar email.",
            tags = "Expert"
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
        return ResponseEntity.ok(expertService.updateEmail(id,email));
    }

    @PatchMapping("/change-password")
    @Operation(
            summary = "Cambiar contraseña",
            description = "Permite cambiar o actualizar la contraseña del experto.",
            tags = "Expert"
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
        return ResponseEntity.ok(expertService.changePassword(newPassword));
    }

    @PatchMapping("/active/{id}")
    @Operation(
            summary = "Asignar estado activo",
            description = "Permite cambiar el estado del experto a activo.",
            tags = "Expert"
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
    public ResponseEntity<String> activeExpert(@PathVariable Long id){
        return ResponseEntity.ok(expertService.expertActive(id));
    }

    @PatchMapping("/inactive/{id}")
    @Operation(
            summary = "Asignar estado inactivo",
            description = "Permite cambiar el estado del experto a inactivo.",
            tags = "Expert"
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
    public ResponseEntity<String> inactiveExpert(@PathVariable Long id){
        return ResponseEntity.ok(expertService.expertInactive(id));
    }

    @PostMapping("/with-resources")
    @Operation(
            summary = "Crea una reserva por experto",
            description = "Crea una reserva especificamente para experto y con recursos si son necesarios",
            tags = "Expert"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva creada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error al crear la reserva"
            )
    })
    public ResponseEntity<ReservationResponse> createReservationWithResourcesRequest (@RequestBody ReservationWithResourcesRequest createReservationWithResourcesRequest) {
        return ResponseEntity.ok().body(expertService.createReservationResourceRequest(createReservationWithResourcesRequest));
    }
}
