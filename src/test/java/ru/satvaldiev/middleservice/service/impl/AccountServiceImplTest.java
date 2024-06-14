package ru.satvaldiev.middleservice.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.satvaldiev.middleservice.client.BackendClient;
import ru.satvaldiev.middleservice.dto.AccountDTO;
import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.response.Response;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private BackendClient backendClient;

    @InjectMocks
    private AccountServiceImpl accountService;
    private AccountDTO account;
    private TelegramUser telegramUser;

    @BeforeEach
    void beforeEach() {
        account = new AccountDTO("Акционный");
        telegramUser = new TelegramUser(123456, "UserName");
    }

    @Test
    void responseTextWhenCreateAccountIsSuccessful() {
        when(backendClient.createAccount(account, telegramUser.userId()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        Response response = accountService.createAccount(account, telegramUser.userId());

        Assertions.assertEquals("Поздравляем! Вы открыли счет \"Акционный\" с 5000 рублей в подарок! " +
                "Информируем Вас, что при смене Вами своего имени пользователя в Telegram, вы незамедлительно " +
                "должны оповестить нас при помощи команды /updateusername", response.getMessage());
    }
    @Test
    void responseTextWhenCreateAccountIsUnsuccessful() {
        when(backendClient.createAccount(account, telegramUser.userId()))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        Response response = accountService.createAccount(account, telegramUser.userId());

        Assertions.assertEquals("Произошла непредвиденная ошибка", response.getMessage());
    }
}