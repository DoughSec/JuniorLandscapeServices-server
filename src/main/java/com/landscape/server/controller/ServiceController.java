package com.landscape.server.controller;

import com.landscape.server.model.Service;
import com.landscape.server.model.dto.service.ServiceRequestDto;
import com.landscape.server.model.dto.service.ServiceResponseDto;
import com.landscape.server.security.SecurityUtils;
import com.landscape.server.service.ServiceLandscapeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/juniorLandscape/services")
public class ServiceController {
    private final ServiceLandscapeService landscapeService;

    public ServiceController(ServiceLandscapeService landscapeService) {
        this.landscapeService = landscapeService;
    }

    //create new service record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ServiceResponseDto create(@RequestBody ServiceRequestDto request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return landscapeService.create(
                currentUserId.intValue(),
                request.getName()
        );
    }

    //get all services
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ServiceResponseDto> getAllServices() {
        return landscapeService.getAll();
    }

    //update Service record
    @PutMapping("/{serviceId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ServiceResponseDto updateService(@PathVariable("serviceId") Integer serviceId, @RequestBody ServiceRequestDto dto) {
        return landscapeService.update(serviceId, dto);
    }

    //delete service record
    @DeleteMapping("/{serviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteService(@PathVariable("serviceId") Integer serviceId) {
        landscapeService.delete(serviceId);
    }

}