package edu.java.clients.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StackOverFlowOwner {
    @JsonProperty("display_name")
    String displayName;
}
