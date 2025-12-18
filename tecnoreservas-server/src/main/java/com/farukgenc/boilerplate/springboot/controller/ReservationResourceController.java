package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.model.UserRole;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationWithResourcesRequest;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationResponse;
import com.farukgenc.boilerplate.springboot.service.ReservationResourceService;
import com.farukgenc.boilerplate.springboot.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation/resources")
@CrossOrigin("http://localhost:5173")
public class ReservationResourceController {

    @Autowired
    private ReservationResourceService reservationResourceService;

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/assign/{reserve}")
    public ResponseEntity<String> assignResourceToReserve(@RequestBody List<Long> resources, @PathVariable Long reserve){
        return ResponseEntity.ok().body(reservationResourceService.assignResource(resources, reserve));
    }

    @PostMapping("/create-reservation-with-resource")
    public ResponseEntity<ReservationResponse> createReservationWithResources(@RequestBody ReservationWithResourcesRequest reservationWithResourcesRequest){
        return ResponseEntity.ok().body(reservationService.createReservationWithResource(reservationWithResourcesRequest));
    }
}