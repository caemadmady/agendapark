package com.farukgenc.boilerplate.springboot.repository;

import com.farukgenc.boilerplate.springboot.model.Reservation;
import com.farukgenc.boilerplate.springboot.model.ServiceLine;
import com.farukgenc.boilerplate.springboot.model.UserRole;
import com.farukgenc.boilerplate.springboot.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByReservationStatus(ReservationStatus status);
    List<Reservation> findAllByExpert_ServiceLine_Id(Long serviceLine);
    List<Reservation> findByDateTimeStartBetween(LocalDateTime date1, LocalDateTime date2);
    List<Reservation> findAllByTalent_Id(Long id);
    List<Reservation> findAllByExpert_Id(Long id);
}
