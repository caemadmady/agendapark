package com.farukgenc.boilerplate.springboot.security.dto.resource;

import com.farukgenc.boilerplate.springboot.model.enums.ResourceStatus;
import com.farukgenc.boilerplate.springboot.model.enums.ResourceType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ResourceListItemResponse {
    private Long id;
    private String name;
    private String description;
    private String plate;
    private String model;
    private String brand;
    private ResourceStatus status;
    private ResourceType resourceType;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Long serviceLineId;
    private String serviceLineName;

    // Campos específicos para BIOTECHNOLOGY
    private Integer maxUsuariosSimultaneos;
    private Map<String, String> condicionesDeUso;
}
