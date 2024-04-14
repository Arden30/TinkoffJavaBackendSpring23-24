package edu.java.schedulers.notifiers;

import edu.java.clients.bot.dto.request.LinkUpdateRequest;

public interface Notifier {
    void notify(LinkUpdateRequest request);
}
