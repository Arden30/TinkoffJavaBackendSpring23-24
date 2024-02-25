package edu.java.clients.stackoverflow;

import edu.java.dto.stackoverflow.StackOverFlowResponse;

public interface StackOverFlowClient {
    StackOverFlowResponse fetchUser(long id);
}
