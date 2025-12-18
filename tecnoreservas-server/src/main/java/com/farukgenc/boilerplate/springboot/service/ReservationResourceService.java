package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.Reservation;
import com.farukgenc.boilerplate.springboot.model.ReservationResource;
import com.farukgenc.boilerplate.springboot.model.Resource;
import com.farukgenc.boilerplate.springboot.repository.ReservationRepository;
import com.farukgenc.boilerplate.springboot.repository.ReservationResourceRepository;
import com.farukgenc.boilerplate.springboot.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationResourceService {

    @Autowired
    private ReservationResourceRepository reservationResourceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    public String assignResource(List<Long> resources, Long reserve){
        Reservation reservation = reservationRepository.findById(reserve).orElseThrow();
        for (Long resource: resources){
            Resource resource1 = resourceRepository.findById(resource).orElseThrow();
            ReservationResource reservationResource = new ReservationResource();
            reservationResource.setReservation(reservation);
            reservationResource.setResource(resource1);
            reservationResourceRepository.save(reservationResource);
        }
        return "Resource assigned to reserve";
    }

}
