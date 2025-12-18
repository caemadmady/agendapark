package com.farukgenc.boilerplate.springboot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Talents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Talent extends User {

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<DigitalRecord> digitalRecords;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<TalentProjectDetail> talentProjectDetails;
    
}
