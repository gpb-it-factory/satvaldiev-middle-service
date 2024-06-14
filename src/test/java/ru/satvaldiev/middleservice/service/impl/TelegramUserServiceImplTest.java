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
import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.response.Response;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TelegramUserServiceImplTest {
    @Mock
    private BackendClient backendClient;
    @InjectMocks
    private TelegramUserServiceImpl telegramUserService;

    private TelegramUser telegramUser;

    @BeforeEach
    void beforeEach() {
        telegramUser = new TelegramUser(123456, "UserName");
    }

    @Test
    void responseTextWhenCreateNewUserIsSuccessful() {
        when(backendClient.createNewUser(telegramUser))
                .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        Response response = telegramUserService.createNewUser(telegramUser);

        Assertions.assertEquals("Поздравляем! Вы стали клиентом нашего банка", response.getMessage());
    }
    @Test
    void responseTextWhenCreateNewUserIsUnsuccessful() {
        when(backendClient.createNewUser(telegramUser))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        Response response = telegramUserService.createNewUser(telegramUser);

        Assertions.assertEquals("Произошла непредвиденная ошибка", response.getMessage());
    }
}