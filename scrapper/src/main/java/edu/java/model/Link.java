package edu.java.model;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class Link {
    private Long id;
    private String url;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
