package edu.java.schedulers.link_processors;

import edu.java.clients.stackoverflow.StackOverFlowClient;
import edu.java.links.response.ParsingResponse;
import edu.java.links.response.StackOverFlowParsingResponse;
import edu.java.model.Link;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackOverflowLinkUpdateProcessorService implements LinkUpdateProcessorService {
    private final StackOverFlowClient stackOverFlowClient;

    @Override
    public Optional<Map.Entry<Link, String>> process(Link link, ParsingResponse resp) {
        if (!(resp instanceof StackOverFlowParsingResponse response)) {
            return Optional.empty();
        }

        var question = stackOverFlowClient.fetchUser(Long.parseLong(response.questionId()));
        if (question.isPresent() && !link.getUpdatedAt().equals(question.get().getUpdatedAt())) {
            link.setUpdatedAt(question.get().getUpdatedAt());
          
            return Optional.of(Map.entry(link, "Question " + question.get().getId() + " was updated"));
        }

        return Optional.empty();
    }
}
