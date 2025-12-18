package com.farukgenc.boilerplate.springboot.service.interfaces;

import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.CreateEquipmentHistoryRequest;
import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.UpdateEquipmentHistoryRequest;
import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.EquipmentHistoryResponse;
import java.util.List;

public interface IEquipmentHistoryService {
    EquipmentHistoryResponse create(CreateEquipmentHistoryRequest request);
    EquipmentHistoryResponse findById(Long id);
    List<EquipmentHistoryResponse> findAllByResourceId(Long resourceId);
    EquipmentHistoryResponse update(Long id, UpdateEquipmentHistoryRequest request);
    void delete(Long id);
}
