package edu.java.bot.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record LinkUpdateRequest(
    int id,
    @NotBlank
    String url,
    @NotBlank
    String description,
    @NotEmpty
    List<Integer> tgChatIds) {
}
