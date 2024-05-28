package ru.satvaldiev.middleservice.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.response.Error;
import ru.satvaldiev.middleservice.response.Response;
import ru.satvaldiev.middleservice.service.TelegramUserService;


@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    private final RestClient restClient;

    public TelegramUserServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Response createNewUser(TelegramUser telegramUser) {

        ResponseEntity<?> responseEntity = createNewUserRequest(telegramUser);

        if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT)) {
            return new Response("Поздравляем! Вы стали клиентом нашего банка");
        }
        Error error = (Error) responseEntity.getBody();
        if (error == null || error.getMessage() == null) {
            return new Response("Произошла непредвиденная ошибка");
        }
        return new Response(error.getMessage());
    }

    public ResponseEntity<?> createNewUserRequest(TelegramUser telegramUser) {
        return restClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(telegramUser)
                .exchange((request, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT)) {
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    } else {
                        Error error = response.bodyTo(Error.class);
                        return new ResponseEntity<>(error, response.getStatusCode());
                    }
                });
    }
}
