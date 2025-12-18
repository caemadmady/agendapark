package com.farukgenc.boilerplate.springboot.service.interfaces;

import com.farukgenc.boilerplate.springboot.model.Resource;
import com.farukgenc.boilerplate.springboot.security.dto.resource.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IResourceService {
    CreateResourceResponse create(CreateResourceRequest request);
    PagedResponse<ResourceListItemResponse> findAll(Pageable pageable, Long serviceLineId);
    UpdateResourceResponse update(Long id, UpdateResourceRequest resourceDetails);
    void delete(Long id);
    void logicalDelete(Long id);
    void logicalEnable(Long id);
    ResourceListItemResponse findByIdAndServiceLine(Long id, Long serviceLineId);
}
