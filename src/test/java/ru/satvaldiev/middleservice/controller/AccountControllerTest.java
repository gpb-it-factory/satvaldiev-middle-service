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
import ru.satvaldiev.middleservice.dto.TransferIncomingDTO;
import ru.satvaldiev.middleservice.dto.TransferOutgoingDTO;
import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.response.Response;
import ru.satvaldiev.middleservice.service.AccountService;


import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    @Test
    void getCurrentBalance_ReturnOk() throws Exception {
        Response response = new Response("5000.00");
        when(accountService.getCurrentBalance(telegramUser.userId()))
                .thenReturn(response);

        mockMvc.perform(
                        get("/users/{id}/accounts", telegramUser.userId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(response.getMessage()));
    }
    @Test
    void transfer_ReturnOk() throws Exception {
        Response response = new Response("Перевод средств выполнен успешно. ID транзакции: " + UUID.randomUUID());
        TransferIncomingDTO transferIncomingDTO = new TransferIncomingDTO(
                telegramUser.userId(), telegramUser.userName(), "Anatoliy", "500.00");
        TransferOutgoingDTO transferOutgoingDTO = new TransferOutgoingDTO(
                telegramUser.userName(), "Anatoliy", "500.00");

        when(accountService.transfer(transferIncomingDTO))
                .thenReturn(response);

        mockMvc.perform(
                        post("/users/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transferOutgoingDTO))
                )
                .andExpect(status().isOk());
    }
}