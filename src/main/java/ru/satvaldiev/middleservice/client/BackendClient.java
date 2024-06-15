package ru.satvaldiev.middleservice.client;

import org.springframework.http.ResponseEntity;
import ru.satvaldiev.middleservice.dto.AccountDTO;
import ru.satvaldiev.middleservice.entity.TelegramUser;

public interface BackendClient {
    ResponseEntity<?> createNewUser(TelegramUser telegramUser);

    ResponseEntity<?> createAccount(AccountDTO account, long id);
}
