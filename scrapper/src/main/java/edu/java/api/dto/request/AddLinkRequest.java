package edu.java.api.dto.request;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record AddLinkRequest(
    @NotNull
    URI link) {
}
