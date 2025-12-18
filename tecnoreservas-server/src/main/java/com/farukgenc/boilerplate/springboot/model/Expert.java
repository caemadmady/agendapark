package com.farukgenc.boilerplate.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Experts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expert extends User {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_line_id", nullable = false)
    @JsonBackReference
    private ServiceLine serviceLine;

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<DigitalRecord> digitalRecords;

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Reservation> reservations;
}
