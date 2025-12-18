package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.Resource;
import com.farukgenc.boilerplate.springboot.model.ServiceLine;
import com.farukgenc.boilerplate.springboot.repository.ResourceRepository;
import com.farukgenc.boilerplate.springboot.repository.ServiceLineRepository;
import com.farukgenc.boilerplate.springboot.security.dto.resource.*;
import com.farukgenc.boilerplate.springboot.security.mapper.resource.ResourceMapper;
import com.farukgenc.boilerplate.springboot.service.interfaces.IResourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResourceService implements IResourceService {

    private final ResourceRepository resourceRepository;
    private final ServiceLineRepository serviceLineRepository;


    public ResourceService(ResourceRepository resourceRepository, ServiceLineRepository serviceLineRepository) {
        this.resourceRepository = resourceRepository;
        this.serviceLineRepository = serviceLineRepository;
    }

    /**
     * Retrieves a resource by its ID, optionally filtered by serviceLineId.
     * Returns DTO with fields according to resource type.
     *
     * @param id The ID of the resource to retrieve
     * @param serviceLineId Optional service line ID to filter the resource
     * @return ResourceListItemResponse or null if not found or not matching service line
     */
    @Override
    public ResourceListItemResponse findByIdAndServiceLine(Long id, Long serviceLineId) {
        Optional<Resource> resourceOpt = resourceRepository.findById(id);
        if (resourceOpt.isEmpty()) {
            return null;
        }
        Resource resource = resourceOpt.get();
        if (serviceLineId != null && (resource.getServiceLine() == null || !serviceLineId.equals(resource.getServiceLine().getId()))) {
            return null;
        }
        return ResourceMapper.mapEntityToListItemResponse(resource);
    }

    /**
     * Creates a new resource based on the provided request data.
     * Handles both BIOTECHNOLOGY and GENERIC resource types using JPA inheritance.
     * 
     * @param request The CreateResourceRequest containing resource data and type discriminator
     * @return CreateResourceResponse with the created resource's id and name
     */
    @Override
    public CreateResourceResponse create(CreateResourceRequest request) {
        // Step 1: Create appropriate entity instance using factory method
        Resource resource = ResourceMapper.createResourceInstance(request.getResourceType());

        // Set ServiceLine based on serviceLineId
        ServiceLine serviceLine = serviceLineRepository.findById(request.getServiceLineId())
                .orElseThrow(() -> new RuntimeException("ServiceLine no encontrada con ID: " + request.getServiceLineId()));

        // Step 2: Populate the entity with data from the request DTO
        ResourceMapper.mapRequestToEntity(request, resource, serviceLine);
        
        // Step 3: Persist the populated entity to database
        Resource savedResource = resourceRepository.save(resource);

        // Step 4: Transform the saved entity back to response DTO
        return ResourceMapper.mapEntityToResponse(savedResource);
    }

    /**
     * Logically enables a resource by setting its 'active' flag to true.
     * The resource is not physically modified except for its status.
     *
     * @param id The ID of the resource to logically enable.
     * @throws RuntimeException if no resource is found with the given ID.
     */
    @Override
    public void logicalEnable(Long id) {
        // Step 1: Find the resource by ID
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));

        // Step 2: Set the 'active' flag to true
        resource.setActive(true);

        // Step 3: Save the updated resource
        resourceRepository.save(resource);
    }

    /**
     * Retrieves a paginated list of all resources.
     * Can be filtered by service line ID.
     *
     * @param pageable      Pagination information (page number, size, sort order)
     * @param serviceLineId Optional ID of the service line to filter resources by
     * @return A PagedResponse containing a list of ResourceListItemResponse
     */
    @Override
    public PagedResponse<ResourceListItemResponse> findAll(Pageable pageable, Long serviceLineId) {
        Page<Resource> resourcePage;
        if (serviceLineId != null) {
            resourcePage = resourceRepository.findAllByServiceLineIdWithFetch(serviceLineId, pageable);
        } else {
            resourcePage = resourceRepository.findAllWithServiceLine(pageable);
        }
    return ResourceMapper.mapPageToPagedResponse(resourcePage);
    }

    /**
     * Updates an existing resource using PATCH operations.
     * Allows partial updates - only provided fields will be modified.
     * Supports both BIOTECHNOLOGY and GENERIC resource types.
     * 
     * @param id The ID of the resource to update
     * @param updateRequest The UpdateResourceRequest containing optional fields to update
     * @return UpdateResourceResponse with complete updated resource information
     */
    @Override
    public UpdateResourceResponse update(Long id, UpdateResourceRequest updateRequest) {
        
        // Step 1: Check if resourceType is provided to determine update strategy
        if (updateRequest.getResourceType() != null) {
            // Step 1a: Create new resource instance of requested type
            Resource newResource = ResourceMapper.createResourceInstance(updateRequest.getResourceType());
            
            // Step 1b: Find existing resource to copy data from
            Resource existingResource = resourceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Resource no encontrado con ID: " + id));

            // Step 1c: Copy existing data to new resource instance
            ResourceMapper.copyResourceFields(existingResource, newResource);

            ServiceLine serviceLine = serviceLineRepository.findById(newResource.getServiceLine().getId())
                    .orElseThrow(() -> new RuntimeException("ServiceLine no encontrada con ID: " + updateRequest.getServiceLineId()));
            System.out.println("Por aqui pasa el codigo despues de buscar el id de serviceLine");

            System.out.println("este es el valor de ServiceLine: "+serviceLine.getId());

            // Step 1d: Apply updates from request
            ResourceMapper.mapUpdateRequestToEntity(updateRequest, newResource, serviceLine);

            // Step 1e: Persist the populated entity to database
            Resource savedResource = resourceRepository.save(newResource);

            // Step 1f: Find existing service line by id
            Optional<ServiceLine> serviceLineByResource = serviceLineRepository.findById(savedResource.getServiceLine().getId());

            // Step 1g: Transform the update entity back to response DTO and return
            return ResourceMapper.mapEntityToUpdateResponse(serviceLineByResource,savedResource);
            
        } else {
            // Step 2: Standard update without type change
            Resource existingResource = resourceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Resource no encontrado con ID: " + id));

            ServiceLine serviceLine = serviceLineRepository.findById(existingResource.getServiceLine().getId())
                    .orElseThrow(() -> new RuntimeException("ServiceLine no encontrada con ID: " + updateRequest.getServiceLineId()));
            
            // Step 2a: Apply partial updates to existing resource
            ResourceMapper.mapUpdateRequestToEntity(updateRequest, existingResource,serviceLine);
            
            // Step 2b: Save updated resource
            Resource savedResource = resourceRepository.save(existingResource);
            Optional<ServiceLine> serviceLineByResource = serviceLineRepository.findById(savedResource.getServiceLine().getId());
            
            return ResourceMapper.mapEntityToUpdateResponse(serviceLineByResource,savedResource);
        }
    }

    @Override
    /**
     * Permanently deletes a resource from the database.
     * The resource is physically removed and cannot be recovered.
     *
     * @param id The ID of the resource to delete.
     * @throws RuntimeException if no resource is found with the given ID.
     */
    public void delete(Long id) {
    // Step 1: Find the resource by ID
    Resource resource = resourceRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));

    // Step 2: Delete the resource physically
    resourceRepository.delete(resource);
    }

    /**
     * Logically deletes a resource by setting its 'active' flag to false.
     * The resource is not physically removed from the database.
     *
     * @param id The ID of the resource to logically delete.
     * @throws RuntimeException if no resource is found with the given ID.
     */
    @Override
    public void logicalDelete(Long id) {
        // Step 1: Find the resource by ID
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));

        // Step 2: Set the 'active' flag to false
        resource.setActive(false);

        // Step 3: Save the updated resource
        resourceRepository.save(resource);
    }
}
