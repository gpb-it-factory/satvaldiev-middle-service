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
import ru.satvaldiev.middleservice.dto.AccountDTO;
import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.response.Response;
import ru.satvaldiev.middleservice.service.AccountService;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;
    private AccountDTO account;
    private TelegramUser telegramUser;

    @BeforeEach
    void beforeEach() {
        account = new AccountDTO("Акционный");
        telegramUser = new TelegramUser(123456, "UserName");
    }
    @Test
    void createAccount_ReturnOk() throws Exception {
        Response response = new Response("Поздравляем! Вы открыли счет \"Акционный\" с 5000 рублей в подарок!");
        when(accountService.createAccount(account, telegramUser.userId()))
                .thenReturn(response);

        mockMvc.perform(
                post("/users/{id}/accounts", telegramUser.userId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account))
        )

                .andExpect(status().isOk());
    }
}