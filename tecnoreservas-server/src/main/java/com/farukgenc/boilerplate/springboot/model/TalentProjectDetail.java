package com.farukgenc.boilerplate.springboot.model;

import com.farukgenc.boilerplate.springboot.model.enums.NameTrl;
import com.farukgenc.boilerplate.springboot.model.enums.ProjectPhase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TalentProjectDetails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TalentProjectDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String associatedProject;

    @Enumerated(EnumType.STRING)
    private ProjectPhase projectPhase;

    @Enumerated(EnumType.STRING)
    private NameTrl nameTrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "talent_id", nullable = false)
    private Talent talent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_line_id", nullable = false)
    private ServiceLine serviceLine;

}
