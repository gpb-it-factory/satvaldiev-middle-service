package ru.satvaldiev.middleservice.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.satvaldiev.middleservice.client.BackendClient;
import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.response.Error;
import ru.satvaldiev.middleservice.response.Response;
import ru.satvaldiev.middleservice.service.TelegramUserService;


@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    private final BackendClient backendClient;

    public TelegramUserServiceImpl(BackendClient backendClient) {
        this.backendClient = backendClient;
    }

    @Override
    public Response createNewUser(TelegramUser telegramUser) {

        ResponseEntity<?> responseEntity = backendClient.createNewUser(telegramUser);

        if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT)) {
            return new Response("Поздравляем! Вы стали клиентом нашего банка");
        }
        if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)) {
            return new Response("Вы уже являетесь клиентом нашего банка");
        }
        Error error = (Error) responseEntity.getBody();
        if (error == null || error.getMessage() == null) {
            return new Response("Произошла непредвиденная ошибка");
        }
        return new Response(error.getMessage());
    }
}
