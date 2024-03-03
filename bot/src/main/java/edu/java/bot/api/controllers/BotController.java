package edu.java.bot.api.controllers;

import edu.java.bot.api.dto.request.LinkUpdateRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bot/api/v1")
@Slf4j
public class BotController {
    @PostMapping("/updates")
    public ResponseEntity<?> addUpdate(@Valid @RequestBody LinkUpdateRequest linkUpdateRequest) {
        log.info("Got request: " + linkUpdateRequest);
        return ResponseEntity.ok().build();
    }
}
