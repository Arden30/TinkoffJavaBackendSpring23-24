package edu.java.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class StackOverFlowResponse {
    @JsonProperty("items")
    List<StackOverFlowQuestion> stackOverFlowQuestions;
}
