package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.EquipmentHistory;
import com.farukgenc.boilerplate.springboot.model.Resource;
import com.farukgenc.boilerplate.springboot.repository.EquipmentHistoryRepository;
import com.farukgenc.boilerplate.springboot.repository.ResourceRepository;
import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.CreateEquipmentHistoryRequest;
import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.UpdateEquipmentHistoryRequest;
import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.EquipmentHistoryResponse;
import com.farukgenc.boilerplate.springboot.security.mapper.equipmenthistory.EquipmentHistoryMapper;
import com.farukgenc.boilerplate.springboot.service.interfaces.IEquipmentHistoryService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EquipmentHistoryService implements IEquipmentHistoryService {
	private final EquipmentHistoryRepository equipmentHistoryRepository;
	private final ResourceRepository resourceRepository;

	public EquipmentHistoryService(EquipmentHistoryRepository equipmentHistoryRepository, ResourceRepository resourceRepository) {
		this.equipmentHistoryRepository = equipmentHistoryRepository;
		this.resourceRepository = resourceRepository;
	}

	/**
	 * Creates a new maintenance history record for a specific resource.
	 * 
	 * This method validates that the resource exists in the database, creates a new
	 * equipment history record associated with that resource, and persists the information
	 * to the database.
	 * 
	 * @param request object containing the necessary data to create the history:
	 *                resourceId (resource ID), eventDate (event date),
	 *                eventType (maintenance event type), details (event details)
	 * @return EquipmentHistoryResponse DTO object with the created history information,
	 *         including the auto-generated ID
	 * @throws RuntimeException if no resource is found with the provided ID
	 */
	@Override
	public EquipmentHistoryResponse create(CreateEquipmentHistoryRequest request) {
		// Buscar el recurso asociado
		Resource resource = resourceRepository.findById(request.getResourceId())
			.orElseThrow(() -> new RuntimeException("Resource not found with ID: " + request.getResourceId()));

		// Crear el historial y asociarlo al recurso
		EquipmentHistory history = EquipmentHistoryMapper.toEntity(resource,request);

		// Guardar el historial
		EquipmentHistory saved = equipmentHistoryRepository.save(history);

		// Mapear a DTO de respuesta
		EquipmentHistoryResponse response = EquipmentHistoryMapper.toResponse(saved);
		return response;
	}

	/**
	 * Retrieves an equipment history record by its unique identifier.
	 * 
	 * This method searches for an equipment history record in the database using
	 * the provided ID and returns the corresponding DTO if found.
	 * 
	 * @param id the unique identifier of the equipment history record to retrieve
	 * @return EquipmentHistoryResponse DTO object containing the history information
	 * @throws RuntimeException if no equipment history is found with the provided ID
	 */
	@Override
	public EquipmentHistoryResponse findById(Long id) {
		EquipmentHistory history = equipmentHistoryRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Equipment history not found with ID: " + id));
		
		return EquipmentHistoryMapper.toResponse(history);
	}

	@Override
	public List<EquipmentHistoryResponse> findAllByResourceId(Long resourceId) {
		return null;
	}

	@Override
	public EquipmentHistoryResponse update(Long id, UpdateEquipmentHistoryRequest request) {
		return null;
	}

	/**
	 * Deletes an equipment history record by its unique identifier.
	 * 
	 * This method first verifies that the equipment history record exists in the database
	 * using the provided ID, then proceeds to delete it permanently. If the record is not
	 * found, it throws a RuntimeException.
	 * 
	 * @param id the unique identifier of the equipment history record to delete
	 * @throws RuntimeException if no equipment history is found with the provided ID
	 */
	@Override
	public void delete(Long id) {
		// Verify that the equipment history exists before attempting to delete
		EquipmentHistory history = equipmentHistoryRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Equipment history not found with ID: " + id));
		
		// Delete the equipment history record
		equipmentHistoryRepository.delete(history);
	}
}
