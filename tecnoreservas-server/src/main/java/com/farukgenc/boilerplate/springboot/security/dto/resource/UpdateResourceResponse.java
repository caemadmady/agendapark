package com.farukgenc.boilerplate.springboot.security.dto.resource;

import com.farukgenc.boilerplate.springboot.model.enums.ResourceStatus;
import com.farukgenc.boilerplate.springboot.model.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for resource update response.
 * Returns complete resource information for both Generic and Biotechnology resources.
 * Includes all fields to allow frontend to update its state without additional calls.
 * 
 * @author Generated
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResourceResponse {

    // Base Resource fields
    private Long id;
    
    private String name;
    
    private String description;
    
    private String plate;
    
    private String model;
    
    private String brand;
    
    private ResourceType resourceType;
    
    private ResourceStatus status;
    
    private LocalDateTime createdDate;
    
    private LocalDateTime updatedDate;
    
    // ServiceLine information
    private Long serviceLineId;
    
    private String serviceLineName;
    
    // BiotechnologyResource specific fields (null for Generic resources)
    private Integer maxUsuariosSimultaneos;
    
    /**
     * JSONB field from BiotechnologyResource containing dynamic key-value pairs
     * for condiciones de uso. Will be null for Generic resources.
     */
    private Map<String, String> condicionesDeUso;
}
