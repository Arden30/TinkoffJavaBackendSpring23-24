package edu.java.bot.model;

import java.net.URI;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
    private long id;
    private State state;
    private List<URI> links;
}
