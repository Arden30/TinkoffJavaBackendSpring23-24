package edu.java.bot.client.dto.response;

import java.net.URI;

public record LinkResponse(
    long id,
    URI url
) {
}
