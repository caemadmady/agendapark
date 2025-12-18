package com.farukgenc.boilerplate.springboot.security.dto.resource;

import com.farukgenc.boilerplate.springboot.model.enums.ResourceStatus;
import com.farukgenc.boilerplate.springboot.model.enums.ResourceType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * DTO for updating resource information using PATCH operations.
 * All fields are optional - only provided fields will be updated.
 * Supports both Generic and Biotechnology resources.
 * 
 * @author Generated
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResourceRequest {

    // Base Resource fields - all optional for PATCH
    @Size(min = 1, max = 255, message = "El nombre debe tener entre 1 y 255 caracteres")
    private String name;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;

    @Size(max = 50, message = "La placa no puede exceder 50 caracteres")
    private String plate;

    @Size(max = 100, message = "El modelo no puede exceder 100 caracteres")
    private String model;

    @Size(max = 100, message = "La marca no puede exceder 100 caracteres")
    private String brand;

    private ResourceType resourceType;

    private ResourceStatus status;

    private Long serviceLineId;

    // BiotechnologyResource specific fields - optional for PATCH
    private Integer maxUsuariosSimultaneos;
    
    /**
     * JSONB field for BiotechnologyResource containing dynamic key-value pairs
     * for condiciones de uso. Only applies to BIOTECHNOLOGY resources.
     * If provided, will replace existing condicionesDeUso completely.
     */
    private Map<String, String> condicionesDeUso;
}
