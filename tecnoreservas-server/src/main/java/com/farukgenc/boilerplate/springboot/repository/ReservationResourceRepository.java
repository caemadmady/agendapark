package com.farukgenc.boilerplate.springboot.repository;

import com.farukgenc.boilerplate.springboot.model.ReservationResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationResourceRepository extends JpaRepository<ReservationResource, Long> {

    List<ReservationResource> findByReservation_Id(Long reservationId);

}
