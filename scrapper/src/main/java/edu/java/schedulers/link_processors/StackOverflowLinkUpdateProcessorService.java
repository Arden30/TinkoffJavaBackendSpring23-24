package edu.java.schedulers.link_processors;

import edu.java.clients.stackoverflow.StackOverFlowClient;
import edu.java.links.response.ParsingResponse;
import edu.java.links.response.StackOverFlowResponse;
import edu.java.model.Link;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackOverflowLinkUpdateProcessorService implements LinkUpdateProcessorService {
    private final StackOverFlowClient stackOverFlowClient;

    @Override
    public Optional<Link> process(Link link, ParsingResponse resp) {
        if (!(resp instanceof StackOverFlowResponse response)) {
            return Optional.empty();
        }

        var question = stackOverFlowClient.fetchUser(Long.parseLong(response.questionId()));
        if (question.isPresent() && !link.getUpdatedAt().equals(question.get().getUpdatedAt())) {
            link.setUpdatedAt(question.get().getUpdatedAt());

            return Optional.of(link);
        }

        return Optional.empty();
    }
}
