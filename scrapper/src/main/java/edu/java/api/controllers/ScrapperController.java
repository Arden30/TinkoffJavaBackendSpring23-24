package edu.java.api.controllers;

import edu.java.api.dto.request.AddLinkRequest;
import edu.java.api.dto.request.RemoveLinkRequest;
import edu.java.api.dto.response.ListLinksResponse;
import edu.java.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("scrapper/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ScrapperController {
    private final UserService userService;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<?> registerChat(@PathVariable("id") long id) {
        userService.registerChat(id);
        return ResponseEntity.ok().body("Chat " + id + " was successfully registered!");
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable("id") long id) {
        userService.deleteChat(id);
        return ResponseEntity.ok().body("Chat with id " + id + " was successfully deleted!");
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long id) {
        return ResponseEntity.ok(userService.getLinks(id));
    }

    @PostMapping("/links")
    public ResponseEntity<?> addLinks(
        @RequestHeader("Tg-Chat-Id") long id,
        @RequestBody @Valid AddLinkRequest request
    ) {
        return ResponseEntity.ok(userService.addLink(id, request.link()));
    }

    @DeleteMapping("/links")
    public ResponseEntity<?> deleteLink(
        @RequestHeader("Tg-Chat-Id") long id,
        @RequestBody @Valid RemoveLinkRequest request
    ) {
        return ResponseEntity.ok(userService.removeLink(id, request.link()));
    }
}
