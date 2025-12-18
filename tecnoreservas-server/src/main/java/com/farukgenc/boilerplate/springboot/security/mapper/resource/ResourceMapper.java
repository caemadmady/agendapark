package com.farukgenc.boilerplate.springboot.security.mapper.resource;

import com.farukgenc.boilerplate.springboot.model.BiotechnologyResource;
import com.farukgenc.boilerplate.springboot.model.GenericResource;
import com.farukgenc.boilerplate.springboot.model.Resource;
import com.farukgenc.boilerplate.springboot.model.ServiceLine;
import com.farukgenc.boilerplate.springboot.model.enums.ResourceType;
import com.farukgenc.boilerplate.springboot.security.dto.resource.*;

import java.util.Optional;

public class ResourceMapper {

    /**
     * Mapea una página de entidades Resource a un DTO paginado estable.
     */
    public static PagedResponse<ResourceListItemResponse> mapPageToPagedResponse(org.springframework.data.domain.Page<Resource> resourcePage) {
        PagedResponse<ResourceListItemResponse> response = new PagedResponse<>();
        response.setContent(resourcePage.map(ResourceMapper::mapEntityToListItemResponse).getContent());
        response.setPage(resourcePage.getNumber());
        response.setSize(resourcePage.getSize());
        response.setTotalElements(resourcePage.getTotalElements());
        response.setTotalPages(resourcePage.getTotalPages());
        response.setLast(resourcePage.isLast());
        return response;
    }

    // =====================================================
    // MÉTODOS PRIVADOS DE MAPEO
    // =====================================================


    /**
     * Mapea una entidad Resource a su DTO para listado paginado.
     */
    public static ResourceListItemResponse mapEntityToListItemResponse(Resource resource) {
        ResourceListItemResponse dto = new ResourceListItemResponse();
        dto.setId(resource.getId());
        dto.setName(resource.getName());
        dto.setDescription(resource.getDescription());
        dto.setPlate(resource.getPlate());
        dto.setModel(resource.getModel());
        dto.setBrand(resource.getBrand());
        dto.setStatus(resource.getStatus());
        dto.setResourceType(resource instanceof BiotechnologyResource ? ResourceType.BIOTECHNOLOGY : ResourceType.GENERIC);
        dto.setCreatedDate(resource.getCreatedDate());
        dto.setUpdatedDate(resource.getUpdatedDate());
        if (resource.getServiceLine() != null) {
            dto.setServiceLineId(resource.getServiceLine().getId());
            dto.setServiceLineName(String.valueOf(resource.getServiceLine().getServiceLineName()));
        }
        if (resource instanceof BiotechnologyResource biotechResource) {
            dto.setMaxUsuariosSimultaneos(biotechResource.getMaxUsuariosSimultaneos());
            dto.setCondicionesDeUso(biotechResource.getCondicionesDeUso());
        }
        return dto;
    }


    /**
     * Maps CreateResourceRequest DTO to Resource entity.
     * Handles common fields for all resource types.
     *
     * @param request The DTO with resource data from client
     * @param resource The entity to populate (BiotechnologyResource or GenericResource)
     */
    public static void mapRequestToEntity(CreateResourceRequest request, Resource resource, ServiceLine serviceLine) {
        // Map common fields
        resource.setName(request.getName());
        resource.setDescription(request.getDescription());
        resource.setPlate(request.getPlate());
        resource.setModel(request.getModel());
        resource.setBrand(request.getBrand());
        resource.setServiceLine(serviceLine);

        // Set default status for new resources
        if (request.getStatus() == null){
            resource.setStatus(com.farukgenc.boilerplate.springboot.model.enums.ResourceStatus.DISPONIBLE);
        }else{
            resource.setStatus(request.getStatus());
        }

        // Handle specific fields for BiotechnologyResource
        if (resource instanceof BiotechnologyResource biotechResource) {
            // Set default values for biotechnology resources
            biotechResource.setMaxUsuariosSimultaneos(3); // Default value from entity
            // condicionesDeUso will be initialized as empty HashMap by entity
            biotechResource.setCondicionesDeUso(request.getCondicionesDeUso());
        }
    }

    /**
     * Maps Resource entity to CreateResourceResponse DTO.
     * Returns minimal response with only id and name.
     *
     * @param resource The saved resource entity
     * @return CreateResourceResponse with id and name
     */
    public static CreateResourceResponse mapEntityToResponse(Resource resource) {
        CreateResourceResponse response = new CreateResourceResponse();
        response.setId(resource.getId());
        response.setName(resource.getName());
        return response;
    }

    /**
     * Factory method to create appropriate Resource instance based on ResourceType.
     * This method centralizes the instantiation logic and prevents code duplication
     * across different service methods (create, update, etc.).
     *
     * @param resourceType The type of resource to instantiate (BIOTECHNOLOGY or GENERIC)
     * @return A new Resource instance of the appropriate subclass
     */
    public static Resource createResourceInstance(ResourceType resourceType) {
        return switch(resourceType) {
            case BIOTECHNOLOGY -> new BiotechnologyResource();
            case GENERIC -> new GenericResource();
        };
    }

    /**
     * Maps UpdateResourceRequest to existing Resource entity for partial updates.
     * Only updates fields that are provided in the request (non-null).
     *
     * @param updateRequest The DTO with optional fields to update
     * @param resource The existing resource entity to update
     */
    public static void mapUpdateRequestToEntity(UpdateResourceRequest updateRequest, Resource resource, ServiceLine serviceLine) {
        // Update common fields only if provided
        if (updateRequest.getName() != null) {
            resource.setName(updateRequest.getName());
        }
        if (updateRequest.getDescription() != null) {
            resource.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getPlate() != null) {
            resource.setPlate(updateRequest.getPlate());
        }
        if (updateRequest.getModel() != null) {
            resource.setModel(updateRequest.getModel());
        }
        if (updateRequest.getBrand() != null) {
            resource.setBrand(updateRequest.getBrand());
        }
        if (updateRequest.getStatus() != null) {
            resource.setStatus(updateRequest.getStatus());
        }

        // Update ServiceLine if provided
        if (updateRequest.getServiceLineId() != null) {
            resource.setServiceLine(serviceLine);
            System.out.println("este es el valor de resource.SetServiceLineId(): "+resource.getServiceLine());
        }

        // Handle BiotechnologyResource specific fields
        if (resource instanceof BiotechnologyResource biotechResource) {
            if (updateRequest.getMaxUsuariosSimultaneos() != null) {
                biotechResource.setMaxUsuariosSimultaneos(updateRequest.getMaxUsuariosSimultaneos());
            }
            if (updateRequest.getCondicionesDeUso() != null) {
                biotechResource.setCondicionesDeUso(updateRequest.getCondicionesDeUso());
            }
        }
    }

    /**
     * Copies all fields from source resource to target resource.
     * Used when changing resource type during update.
     *
     * @param source The source resource to copy from
     * @param target The target resource to copy to
     */
    public static void copyResourceFields(Resource source, Resource target) {
        target.setId(source.getId());
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setPlate(source.getPlate());
        target.setModel(source.getModel());
        target.setBrand(source.getBrand());
        target.setStatus(source.getStatus());
        target.setServiceLine(source.getServiceLine());
        target.setCreatedDate(source.getCreatedDate());
        target.setUpdatedDate(source.getUpdatedDate());

        // Copiar campos específicos de BiotechnologyResource
        if (source instanceof com.farukgenc.boilerplate.springboot.model.BiotechnologyResource srcBio &&
            target instanceof com.farukgenc.boilerplate.springboot.model.BiotechnologyResource tgtBio) {
            tgtBio.setMaxUsuariosSimultaneos(srcBio.getMaxUsuariosSimultaneos());
            tgtBio.setCondicionesDeUso(srcBio.getCondicionesDeUso());
        }
    }

    /**
     * Maps Resource entity to UpdateResourceResponse DTO.
     * Returns complete resource information including type-specific fields.
     *
     * @param resource The updated resource entity
     * @return UpdateResourceResponse with all resource information
     */
    public static UpdateResourceResponse mapEntityToUpdateResponse(Optional<ServiceLine> serviceLine, Resource resource) {
        UpdateResourceResponse response = new UpdateResourceResponse();

        // Map common fields
        response.setId(resource.getId());
        response.setName(resource.getName());
        response.setDescription(resource.getDescription());
        response.setPlate(resource.getPlate());
        response.setModel(resource.getModel());
        response.setBrand(resource.getBrand());
        response.setStatus(resource.getStatus());
        response.setCreatedDate(resource.getCreatedDate());
        response.setUpdatedDate(resource.getUpdatedDate());

        // Determine resource type using instanceof
        if (resource instanceof BiotechnologyResource biotechResource) {
            response.setResourceType(com.farukgenc.boilerplate.springboot.model.enums.ResourceType.BIOTECHNOLOGY);
            response.setMaxUsuariosSimultaneos(biotechResource.getMaxUsuariosSimultaneos());
            response.setCondicionesDeUso(biotechResource.getCondicionesDeUso());
        } else if (resource instanceof GenericResource) {
            response.setResourceType(com.farukgenc.boilerplate.springboot.model.enums.ResourceType.GENERIC);
        }

        // Map ServiceLine information
        if (resource.getServiceLine() != null && serviceLine.isPresent()) {
            response.setServiceLineId(resource.getServiceLine().getId());
            response.setServiceLineName(String.valueOf(serviceLine.get().getServiceLineName()));
        }

        return response;
    }
}

