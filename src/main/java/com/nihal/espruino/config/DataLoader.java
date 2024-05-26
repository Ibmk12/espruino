package com.nihal.espruino.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nihal.espruino.model.Measurement;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
@NoArgsConstructor
public class DataLoader {

    public List<Measurement> loadData() {
        try {
            ClassPathResource resource = new ClassPathResource("measurements.json");
            byte[] jsonData = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String jsonString = new String(jsonData, StandardCharsets.UTF_8);

            // Parse JSON into objects
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, Measurement.class));
        } catch (Exception ex) {
            log.error("Error retrieving the data.. ", ex);
            return null;
        }
    }
}
