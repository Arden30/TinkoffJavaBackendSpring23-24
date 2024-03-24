package edu.java.clients.stackoverflow;

import edu.java.clients.stackoverflow.dto.StackOverFlowQuestion;
import java.util.Optional;

public interface StackOverFlowClient {
    Optional<StackOverFlowQuestion> fetchUser(long id);
}
