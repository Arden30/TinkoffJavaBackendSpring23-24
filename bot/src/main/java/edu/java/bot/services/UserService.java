package edu.java.bot.services;

import edu.java.bot.model.State;
import edu.java.bot.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public boolean register(long id) {
        Optional<User> userOptional = findById(id);

        if (userOptional.isEmpty()) {
            User user = new User();
            user.setId(id);
            user.setState(State.DEFAULT);
            user.setLinks(new ArrayList<>());
            addUser(user);

            return true;
        }

        return false;
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void deleteUser(User user) {
        users.remove(user);
    }

    public Optional<User> findById(long id) {
        return users.containsKey(id) ? Optional.of(users.get(id)) : Optional.empty();
    }

    public void changeState(long id, State state) {
        Optional<User> userOptional = findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            deleteUser(user);
            user.setState(state);
            addUser(user);
        }
    }

}
