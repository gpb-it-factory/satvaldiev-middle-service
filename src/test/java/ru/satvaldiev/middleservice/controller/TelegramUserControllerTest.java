package ru.satvaldiev.middleservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.response.Response;
import ru.satvaldiev.middleservice.service.TelegramUserService;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = TelegramUserController.class)
@ExtendWith(MockitoExtension.class)
class TelegramUserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TelegramUserService telegramUserService;

    @Autowired
    private ObjectMapper objectMapper;
    private TelegramUser telegramUser;

    @BeforeEach
    void beforeEach() {
        telegramUser = new TelegramUser(123456, "UserName");
    }
    @Test
    void createNewUser_ReturnOk() throws Exception {
        Response response = new Response("Поздравляем! Вы стали клиентом нашего банка");
        when(telegramUserService.createNewUser(telegramUser))
                .thenReturn(response);

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(telegramUser))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(response.getMessage()));
    }
}