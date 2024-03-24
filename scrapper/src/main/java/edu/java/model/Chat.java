package edu.java.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.OffsetDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
public class Chat {
    @Id
    private Long id;
    @CreationTimestamp
    private OffsetDateTime createdAt;
}
