package edu.java.api.controllers;

import edu.java.api.dto.request.AddLinkRequest;
import edu.java.api.dto.request.RemoveLinkRequest;
import edu.java.api.dto.response.LinkResponse;
import edu.java.api.dto.response.ListLinksResponse;
import edu.java.services.ChatService;
import edu.java.services.LinkService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
    private final ChatService chatService;
    private final LinkService linkService;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<?> registerChat(@PathVariable("id") long id) {
        chatService.register(id);
        return ResponseEntity.ok().body("Chat " + id + " was successfully registered!");
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable("id") long id) {
        chatService.unregister(id);
        return ResponseEntity.ok().body("Chat with id " + id + " was successfully deleted!");
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long id) {
        List<LinkResponse> links = linkService
            .listAll(id)
            .stream()
            .map(link -> new LinkResponse(link.getId(), URI.create(link.getUrl())))
            .toList();

        return ResponseEntity.ok(new ListLinksResponse(links));
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLinks(
        @RequestHeader("Tg-Chat-Id") long id,
        @RequestBody @Valid AddLinkRequest request
    ) {
        linkService.add(id, request.link().toString());

        return ResponseEntity.ok(new LinkResponse(id, request.link()));
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader("Tg-Chat-Id") long id,
        @RequestBody @Valid RemoveLinkRequest request
    ) {
        linkService.remove(id, request.link().toString());

        return ResponseEntity.ok(new LinkResponse(id, request.link()));
    }
}
