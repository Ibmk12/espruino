package com.nihal.espruino.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class MagData {

    @Embedded
    Coordinations data;
}
