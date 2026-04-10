package com.landscape.server.service;

import com.landscape.server.exception.BadRequestException;
import com.landscape.server.exception.ResourceNotFoundException;
import com.landscape.server.model.dto.service.ServiceRequestDto;
import com.landscape.server.model.dto.service.ServiceResponseDto;
import com.landscape.server.model.Service;
import com.landscape.server.repository.ServiceRepository;
import com.landscape.server.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@org.springframework.stereotype.Service
@Transactional
public class ServiceLandscapeService {

    private final ServiceRepository serviceRepository;

    public ServiceLandscapeService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // create Service
    public ServiceResponseDto create(
            Integer userId, String name
    ) {
        if (userId == null) {
            throw new BadRequestException("userId is required");
        }

        Service service = new Service();
        service.setName(name);

        serviceRepository.save(service);

        ServiceResponseDto responseDto = new ServiceResponseDto();
        responseDto.setId(service.getId());
        responseDto.setName(name);

        return responseDto;
    }

    // getAll
    @Transactional(readOnly = true)
    public List<ServiceResponseDto> getAll() {
        List<Service> services = serviceRepository.findAll();
        return services.stream().map(service -> {
            ServiceResponseDto dto = new ServiceResponseDto();
            dto.setId(service.getId());
            dto.setName(service.getName());
            return dto;
        }).toList();
    }

    // getById
    @Transactional(readOnly = true)
    public Service getById(Integer serviceId) {
        if (serviceId == null) {
            throw new BadRequestException("ServiceId is required");
        }
        return serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + serviceId));
    }

    // update service
    public ServiceResponseDto update(Integer serviceId, ServiceRequestDto updated) {
        Service existing = getById(serviceId);

        existing.setName(updated.getName());

        serviceRepository.save(existing);

        ServiceResponseDto responseDto = new ServiceResponseDto();
        responseDto.setId(existing.getId());
        responseDto.setName(existing.getName());

        return responseDto;
    }

    // delete Service
    public void delete(Integer serviceId) {
        if (serviceId == null) {
            throw new BadRequestException("ServiceId is required");
        }
        if (!serviceRepository.existsById(serviceId)) {
            throw new ResourceNotFoundException("Service not found: " + serviceId);
        }

        serviceRepository.deleteById(serviceId);
    }
}

