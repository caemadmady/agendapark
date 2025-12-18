package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.CreateEquipmentHistoryRequest;
import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.EquipmentHistoryResponse;
import com.farukgenc.boilerplate.springboot.service.EquipmentHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipment/histories")
public class EquipmentHistoryController {

	private final EquipmentHistoryService equipmentHistoryService;

	@Autowired
	public EquipmentHistoryController(EquipmentHistoryService equipmentHistoryService) {
		this.equipmentHistoryService = equipmentHistoryService;
	}

	/**
	 * Crea un nuevo registro de historial de mantenimiento para un recurso.
	 * @param request Datos del mantenimiento
	 * @return Registro creado
	 */

    @Operation(
            summary = "Crear historial de mantenimiento",
            description = "Crea un nuevo registro de historial de mantenimiento para un recurso."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Historial creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EquipmentHistoryResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
	@PostMapping
	public ResponseEntity<EquipmentHistoryResponse> createEquipmentHistory(@RequestBody CreateEquipmentHistoryRequest request) {
		EquipmentHistoryResponse response = equipmentHistoryService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * Retrieves an equipment history record by its ID.
	 * @param id ID of the equipment history record
	 * @return Equipment history record details
	 */
	@Operation(
			summary = "Obtener historial de mantenimiento por ID",
			description = "Obtiene un registro específico de historial de mantenimiento mediante su identificador único."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Historial de mantenimiento encontrado exitosamente",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = EquipmentHistoryResponse.class)
					)
			),
			@ApiResponse(responseCode = "404", description = "Historial de mantenimiento no encontrado")
	})
	@GetMapping("/{id}")
	public ResponseEntity<EquipmentHistoryResponse> getEquipmentHistoryById(@PathVariable Long id) {
		EquipmentHistoryResponse response = equipmentHistoryService.findById(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * Deletes an equipment history record by its ID.
	 * @param id ID of the equipment history record to delete
	 */
	@Operation(
			summary = "Eliminar historial de mantenimiento",
			description = "Elimina un registro específico de historial de mantenimiento mediante su identificador único."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "204",
					description = "Historial de mantenimiento eliminado exitosamente"
			),
			@ApiResponse(responseCode = "404", description = "Historial de mantenimiento no encontrado")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEquipmentHistory(@PathVariable Long id) {
		equipmentHistoryService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
