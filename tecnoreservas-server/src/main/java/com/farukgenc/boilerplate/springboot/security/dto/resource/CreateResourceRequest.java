package com.farukgenc.boilerplate.springboot.security.dto.resource;

import com.farukgenc.boilerplate.springboot.model.ServiceLine;
import com.farukgenc.boilerplate.springboot.model.enums.ResourceStatus;
import com.farukgenc.boilerplate.springboot.model.enums.ResourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for creating resource requests.
 * Contains validation annotations for automatic validation.
 * 
 * @author Generated
 */
@Data
public class CreateResourceRequest {

    /**
     * Resource name. Required field with length validation.
     */
    @NotBlank(message = "El nombre del recurso es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    /**
     * Resource description. Optional field.
     */
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String description;

    /**
     * Resource plate/identifier. Required and unique.
     */
    @NotBlank(message = "La placa del recurso es obligatoria")
    @Size(min = 3, max = 20, message = "La placa debe tener entre 3 y 20 caracteres")
    private String plate;

    /**
     * Resource model. Required field.
     */
    @NotBlank(message = "El modelo del recurso es obligatorio")
    @Size(min = 2, max = 50, message = "El modelo debe tener entre 2 y 50 caracteres")
    private String model;

    /**
     * Resource brand. Required field.
     */
    @NotBlank(message = "La marca del recurso es obligatoria")
    @Size(min = 2, max = 50, message = "La marca debe tener entre 2 y 50 caracteres")
    private String brand;

    /**
     * Resource type. Required field for determining the type of resource to create.
     */
    @NotNull(message = "El tipo de recurso es obligatorio")
    private ResourceType resourceType;

    /**
     * Resource type. Required field for determining the type of resource to create.
     */
    private ResourceStatus status;

    /**
     * Service line ID. Required for resource classification.
     */
    @NotNull(message = "El ID de línea de servicio es obligatorio")
    private Long serviceLineId;

    /**
     * Número máximo de usuarios simultáneos (solo para BIOTECHNOLOGY).
     */
    private Integer maxUsuariosSimultaneos;

    /**
     * Condiciones de uso interactivas (solo para BIOTECHNOLOGY).
     */
    private java.util.Map<String, String> condicionesDeUso;
}
