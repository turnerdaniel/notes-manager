package uk.co.danielturner.restapi.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.danielturner.restapi.models.User;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    void returnsCorrectName() {
        final String name = "Example";
        final User user = userController.getUsers(name, "");

        Assertions.assertThat(user.getName()).isEqualTo(name);
    }

    @Test
    void returnsCorrectNumber() {
        final String number = "01234";
        final User user = userController.getUsers("", number);

        Assertions.assertThat(user.getPhoneNo()).isEqualTo(number);
    }

    @Test
    void returnsDefault() {
        // Potentially wrong way of testing? Need to create mock HTTP requests?
        final User user = userController.getUsers();

        Assertions.assertThat(user.getName()).isEqualTo("Example");
        Assertions.assertThat(user.getPhoneNo()).isEqualTo("0123456789");
    }
}