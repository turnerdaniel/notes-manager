package uk.co.danielturner.restapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.danielturner.restapi.models.User;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class UserController {
    private final AtomicLong id = new AtomicLong();

    @GetMapping("/users")
    public User getUsers() {
        return new User(id.incrementAndGet(), "Test", "0123456789");
    }
}
