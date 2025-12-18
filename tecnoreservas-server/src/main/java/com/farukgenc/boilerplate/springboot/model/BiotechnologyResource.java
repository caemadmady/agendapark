package com.farukgenc.boilerplate.springboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

@Entity
@DiscriminatorValue("BIOTECHNOLOGY")
@Getter
@Setter
public class BiotechnologyResource extends Resource {

    @Column(nullable = false)
    private int maxUsuariosSimultaneos = 3;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> condicionesDeUso = new HashMap<>();
}