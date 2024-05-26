package com.nihal.espruino.dto;

import com.nihal.espruino.model.AccelData;
import com.nihal.espruino.model.Measurement;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CorrelationRequest {

    List<Measurement> measurements;
    String indicator1;
    String indicator2;
}
