package edu.java.clients.stackoverflow;

import edu.java.clients.stackoverflow.dto.StackOverFlowResponse;

public interface StackOverFlowClient {
    StackOverFlowResponse fetchUser(long id);
}
