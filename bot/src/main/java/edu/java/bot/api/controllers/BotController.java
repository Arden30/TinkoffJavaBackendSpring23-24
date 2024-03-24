package edu.java.bot.api.controllers;

import edu.java.bot.api.dto.request.LinkUpdateRequest;
import edu.java.bot.services.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bot/api/v1")
@Slf4j
@RequiredArgsConstructor
public class BotController {
    private final NotificationService notificationService;

    @PostMapping("/updates")
    public void addUpdate(@Valid @RequestBody LinkUpdateRequest linkUpdateRequest) {
        notificationService.notify(linkUpdateRequest);
    }
}
