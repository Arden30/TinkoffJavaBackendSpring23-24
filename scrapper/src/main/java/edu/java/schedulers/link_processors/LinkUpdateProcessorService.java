package edu.java.schedulers.link_processors;

import edu.java.model.Link;
import edu.java.response.ParsingResponse;
import java.util.Map;
import java.util.Optional;

public interface LinkUpdateProcessorService {
    Optional<Map.Entry<Link, String>> process(Link link, ParsingResponse resp);
}
