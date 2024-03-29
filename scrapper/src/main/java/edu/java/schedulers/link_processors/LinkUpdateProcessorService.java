package edu.java.schedulers.link_processors;

import edu.java.links.response.ParsingResponse;
import edu.java.model.Link;
import java.util.Map;
import java.util.Optional;

public interface LinkUpdateProcessorService {
    Optional<Map.Entry<Link, String>> process(Link link, ParsingResponse resp);
}
