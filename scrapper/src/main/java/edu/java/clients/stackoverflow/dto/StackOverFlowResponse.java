package edu.java.clients.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class StackOverFlowResponse {
    @JsonProperty("items")
    List<StackOverFlowQuestion> stackOverFlowQuestions;
}
