package com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory;

import com.farukgenc.boilerplate.springboot.model.enums.HistoryEventType;
import lombok.Data;
import java.util.Date;

@Data
public class UpdateEquipmentHistoryRequest {
    private Date eventDate;
    private HistoryEventType eventType;
    private String details;
}
