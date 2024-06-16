package ru.satvaldiev.middleservice.client.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.satvaldiev.middleservice.client.BackendClient;
import ru.satvaldiev.middleservice.dto.AccountDTO;
import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.response.Error;

@Component
public class BackendClientImpl implements BackendClient {
    private final RestClient restClient;

    public BackendClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public ResponseEntity<?> createNewUser(TelegramUser telegramUser) {
        return restClient.post()
                .uri("/v2/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(telegramUser)
                .exchange((request, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT)) {
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    } else if (response.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)) {
                        return new ResponseEntity<>(HttpStatus.CONFLICT);
                    }
                    else {
                        Error error = response.bodyTo(Error.class);
                        return new ResponseEntity<>(error, response.getStatusCode());
                    }
                });
    }

    @Override
    public ResponseEntity<?> createAccount(AccountDTO account, long id) {
        return restClient.post()
                .uri("/v2/users/{id}/accounts", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(account)
                .exchange((request, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT)) {
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    } else if (response.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)) {
                        return new ResponseEntity<>(HttpStatus.CONFLICT);
                    }
                    else {
                        Error error = response.bodyTo(Error.class);
                        return new ResponseEntity<>(error, response.getStatusCode());
                    }
                });
    }
}
