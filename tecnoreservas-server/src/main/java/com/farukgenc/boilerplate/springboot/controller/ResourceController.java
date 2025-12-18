package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceRequest;
import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceResponse;
import com.farukgenc.boilerplate.springboot.security.dto.resource.UpdateResourceRequest;
import com.farukgenc.boilerplate.springboot.security.dto.resource.UpdateResourceResponse;
import com.farukgenc.boilerplate.springboot.security.dto.resource.ResourceListItemResponse;
import com.farukgenc.boilerplate.springboot.security.dto.resource.PagedResponse;
import com.farukgenc.boilerplate.springboot.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Resource operations.
 * Handles HTTP requests related to resource management.
 * 
 * @author Generated
 */
@RestController
@RequestMapping("/resources")
@CrossOrigin("*")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * Retrieves a resource by its ID, optionally filtered by serviceLineId.
     * Returns DTO with fields according to resource type.
     *
     * @param id The ID of the resource to retrieve
     * @param serviceLineId Optional service line ID to filter the resource
     * @return ResponseEntity containing the resource or 404 if not found or not matching service line
     */
    @Operation(
            summary = "Obtener recurso por ID",
            description = "Devuelve un recurso por su identificador. Permite filtrar por línea de servicio (serviceLineId)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Recurso encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResourceListItemResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado o no coincide con la línea de servicio")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResourceListItemResponse> getResourceById(
            @PathVariable Long id,
            @RequestParam(required = false) Long serviceLineId
    ) {
        ResourceListItemResponse response = resourceService.findByIdAndServiceLine(id, serviceLineId);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new resource.
     * 
     * @param createRequest The resource creation request with validation
     * @return ResponseEntity containing the created resource response with HTTP 201 status
     */

    @Operation(
            summary = "Crear un nuevo recurso",
            description = "Crea un recurso en el sistema. El tipo de recurso se determina por los campos enviados en el cuerpo de la solicitud."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Recurso creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateResourceResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping
    public ResponseEntity<CreateResourceResponse> createResource(@Valid @RequestBody CreateResourceRequest createRequest) {
        CreateResourceResponse createdResource = resourceService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdResource);
    }

    /**
     * Updates an existing resource using PATCH operation.
     * Allows partial updates - only provided fields will be modified.
     * Supports both Generic and Biotechnology resources.
     * 
     * @param id The ID of the resource to update
     * @param updateRequest The resource update request with optional fields and validation
     * @return ResponseEntity containing the complete updated resource response with HTTP 200 status
     */

    @Operation(
            summary = "Actualizar parcialmente un recurso existente",
            description = "Permite modificar los campos de un recurso existente (genérico o biotecnológico)"+
                            "mediante una operación PATCH. Solo los campos enviados en la solicitud serán actualizados."+
                                "Requiere autenticación y validación."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Recurso actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateResourceResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UpdateResourceResponse> updateResource(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateResourceRequest updateRequest) {
        UpdateResourceResponse updatedResource = resourceService.update(id, updateRequest);
        return ResponseEntity.ok(updatedResource);
    }

    /**
     * Retrieves a paginated list of resources with stable JSON structure.
     * Returns DTOs with fields according to resource type.
     *
     * @param page Page number (default 0)
     * @param size Page size (default 10)
     * @return Paginated list of ResourceListItemResponse wrapped in PagedResponse
     */

    @Operation(
            summary = "Obtener lista paginada de recursos",
            description = "Devuelve una lista paginada de recursos registrados en el sistema."+
                            "Permite filtrar por línea de servicio (serviceLineId). "+
                                "La respuesta incluye información relevante según el tipo de recurso."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista paginada de recursos obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagedResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<PagedResponse<ResourceListItemResponse>> getResources(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long serviceLineId) {
        PagedResponse<ResourceListItemResponse> response = resourceService.findAll(PageRequest.of(page, size),serviceLineId);
        return ResponseEntity.ok(response);
    }

    /**
     * Logically deletes a resource by setting its status to inactive.
     * The resource is not physically removed from the database.
     *
     * @param id The ID of the resource to logically delete
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @Operation(
            summary = "Borrar lógicamente un recurso",
            description = "Marca un recurso como inactivo en el sistema sin eliminarlo físicamente de la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Recurso marcado como inactivo exitosamente"
            ),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @DeleteMapping("/{id}/disable")
    public ResponseEntity<Void> logicalDeleteResource(@PathVariable Long id) {
        resourceService.logicalDelete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Permanently deletes a resource from the database.
     * The resource is physically removed.
     *
     * @param id The ID of the resource to delete
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @Operation(
            summary = "Eliminar definitivamente un recurso",
            description = "Elimina físicamente un recurso de la base de datos. Esta operación no se puede deshacer."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Recurso eliminado exitosamente"
            ),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Logically enables a resource by setting its status to active.
     * The resource is not physically modified except for its status.
     *
     * @param id The ID of the resource to logically enable
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @Operation(
            summary = "Habilitar lógicamente un recurso",
            description = "Marca un recurso como activo en el sistema sin modificar otros datos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Recurso habilitado exitosamente"
            ),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PatchMapping("/{id}/enable")
    public ResponseEntity<Void> logicalEnableResource(@PathVariable Long id) {
        resourceService.logicalEnable(id);
        return ResponseEntity.noContent().build();
    }
}
