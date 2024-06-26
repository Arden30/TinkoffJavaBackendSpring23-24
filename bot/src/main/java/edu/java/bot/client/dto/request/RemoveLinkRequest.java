package edu.java.bot.client.dto.request;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record RemoveLinkRequest(
    @NotBlank
    String link) {
}
