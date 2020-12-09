package uk.co.danielturner.restapi.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mock;

    @Test
    void returnsCorrectName() throws Exception {
        final String param = "Testing";
        final String url = "/users?name=" + param;

        mock.perform(get(url)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(param));
    }

    @Test
    void returnsCorrectNumber() throws Exception {
        final String param = "01234";
        final String url = "/users?number=" + param;

        mock.perform(get(url)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(param));
    }

    @Test
    void returnsDefault() throws Exception {
        mock.perform(get("/users")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Example"))
                .andExpect(jsonPath("$.number").value("0123456789"));
    }
}