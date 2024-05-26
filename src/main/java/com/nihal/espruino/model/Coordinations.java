package com.nihal.espruino.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Coordinations {

    @Column(insertable=false, updatable=false)
    private float x;
    @Column(insertable=false, updatable=false)
    private float y;
    @Column(insertable=false, updatable=false)
    private float z;

}
