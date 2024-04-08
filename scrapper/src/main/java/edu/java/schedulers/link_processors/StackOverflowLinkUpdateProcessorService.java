package edu.java.schedulers.link_processors;

import edu.java.clients.stackoverflow.StackOverFlowClient;
import edu.java.model.Link;
import edu.java.repository.LinkRepository;
import edu.java.response.ParsingResponse;
import edu.java.response.StackOverFlowParsingResponse;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackOverflowLinkUpdateProcessorService implements LinkUpdateProcessorService {
    private final StackOverFlowClient stackOverFlowClient;
    private final LinkRepository linkRepository;

    @Override
    public Optional<Map.Entry<Link, String>> process(Link link, ParsingResponse resp) {
        if (!(resp instanceof StackOverFlowParsingResponse response)) {
            return Optional.empty();
        }

        var question = stackOverFlowClient.fetchUser(Long.parseLong(response.questionId()));

        if (question.isPresent() && link.getUpdatedAt().equals(OffsetDateTime.MIN)) {
            linkRepository.saveChanges(link);
            link.setUpdatedAt(question.get().getUpdatedAt());
        }

        if (question.isPresent() && !link.getUpdatedAt().equals(question.get().getUpdatedAt())) {
            link.setUpdatedAt(question.get().getUpdatedAt());

            return Optional.of(Map.entry(link, "Question " + question.get().getId() + " was updated"));
        }

        return Optional.empty();
    }
}
