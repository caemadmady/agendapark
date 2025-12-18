package com.farukgenc.boilerplate.springboot.security.mapper.equipmenthistory;

import com.farukgenc.boilerplate.springboot.model.EquipmentHistory;
import com.farukgenc.boilerplate.springboot.model.Resource;
import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.EquipmentHistoryResponse;
import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.CreateEquipmentHistoryRequest;
import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.UpdateEquipmentHistoryRequest;

public class EquipmentHistoryMapper {
    public static EquipmentHistoryResponse toResponse(EquipmentHistory entity) {
        EquipmentHistoryResponse dto = new EquipmentHistoryResponse();
        dto.setId(entity.getId());
        dto.setResourceId(entity.getResource().getId());
        dto.setEventDate(entity.getEventDate());
        dto.setEventType(entity.getEventType());
        dto.setDetails(entity.getDetails());
        return dto;
    }

    public static EquipmentHistory toEntity(Resource resourceId,CreateEquipmentHistoryRequest request) {
        EquipmentHistory entity = new EquipmentHistory();
        entity.setEventDate(request.getEventDate());
        entity.setEventType(request.getEventType());
        entity.setDetails(request.getDetails());
        entity.setResource(resourceId);
        return entity;
    }

    public static void updateEntity(UpdateEquipmentHistoryRequest request, EquipmentHistory entity) {
        entity.setEventDate(request.getEventDate());
        entity.setEventType(request.getEventType());
        entity.setDetails(request.getDetails());
    }
}
