package edu.java.bot.model;

import com.pengrad.telegrambot.request.BaseRequest;
import lombok.Builder;

@Builder
public record UserRequest(BaseRequest<?, ?> request) {

}
