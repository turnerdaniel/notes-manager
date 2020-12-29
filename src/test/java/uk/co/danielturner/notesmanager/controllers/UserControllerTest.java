package uk.co.danielturner.notesmanager.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class NoteControllerTest {

    @Autowired
    private MockMvc mock;

    @Test
    void returnsCorrectTitle() throws Exception {
        final String param = "Testing";
        final String url = "/create?title=" + param;

        mock.perform(post(url)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(param));
    }

    @Test
    void returnsCorrectDescription() throws Exception {
        final String param = "abcdefg";
        final String url = "/create?description=" + param;

        mock.perform(post(url)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(param));
    }

    @Test
    void returnsDefault() throws Exception {
        mock.perform(post("/create")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Add title here"))
                .andExpect(jsonPath("$.description").value(""));
    }
}