package com.farukgenc.boilerplate.springboot.repository;

import com.farukgenc.boilerplate.springboot.model.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    @Query("SELECT r FROM Resource r JOIN FETCH r.serviceLine WHERE r.active = true")
    Page<Resource> findAllWithServiceLine(Pageable pageable);

    @Query("SELECT r FROM Resource r JOIN FETCH r.serviceLine sl WHERE sl.id = :serviceLineId AND r.active = true")
    Page<Resource> findAllByServiceLineIdWithFetch(@Param("serviceLineId") Long serviceLineId, Pageable pageable);

    Page<Resource> findAllByServiceLineId(Long serviceLineId, Pageable pageable);
}
