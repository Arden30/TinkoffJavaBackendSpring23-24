package edu.java.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class GitHubRepo {
    @Id
    private Long linkId;
    private Long stars;
    private Long issues;
}
