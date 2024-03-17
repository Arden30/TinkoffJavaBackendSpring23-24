package edu.java.bot.services;

import edu.java.bot.client.ScrapperWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ScrapperWebClient scrapperWebClient;

    public boolean register(long id) {
        return scrapperWebClient.registerChat(id);
    }

    public boolean unregister(long id) {
        return scrapperWebClient.deleteChat(id);
    }

}
