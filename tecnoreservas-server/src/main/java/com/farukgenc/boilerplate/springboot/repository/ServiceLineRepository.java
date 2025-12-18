package com.farukgenc.boilerplate.springboot.repository;

import com.farukgenc.boilerplate.springboot.model.Resource;
import com.farukgenc.boilerplate.springboot.model.ServiceLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceLineRepository extends JpaRepository<ServiceLine, Long> {

    @Query("SELECT r FROM Resource r JOIN FETCH r.serviceLine WHERE r.id = :id")
    Optional<Resource> findByIdWithServiceLine(Long id);

}
