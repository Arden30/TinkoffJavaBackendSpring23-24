package edu.java.clients.bot.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record LinkUpdateRequest(
    long id,
    @NotNull
    URI url,
    @NotBlank
    String description,
    @NotEmpty
    List<Integer> tgChatIds) {
}
