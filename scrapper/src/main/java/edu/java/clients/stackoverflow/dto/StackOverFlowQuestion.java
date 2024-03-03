package edu.java.clients.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class StackOverFlowQuestion {
    @JsonProperty("owner")
    StackOverFlowOwner stackOverFlowOwner;
    @JsonProperty("creation_date")
    OffsetDateTime creationDate;
    @JsonProperty("id")
    Long id;
}
