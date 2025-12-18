package com.farukgenc.boilerplate.springboot.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("GENERIC")
@Getter
@Setter
public class GenericResource extends Resource {
    // Por ahora no tiene campos adicionales, pero está listo para extenderse.
}
