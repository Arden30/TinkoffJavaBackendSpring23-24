package edu.java.model;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class Chat {
    private Long id;
    private OffsetDateTime createdAt;
}
