package edu.java.clients.bot.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import lombok.Builder;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Builder
public record LinkUpdateRequest(
    long id,
    @NotNull
    URI url,
    @NotBlank
    String description,
    @NotEmpty
    List<Long> tgChatIds) {
}
