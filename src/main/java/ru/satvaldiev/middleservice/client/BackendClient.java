package ru.satvaldiev.middleservice.client;

import org.springframework.http.ResponseEntity;
import ru.satvaldiev.middleservice.dto.AccountDTO;
import ru.satvaldiev.middleservice.dto.TransferOutgoingDTO;
import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.response.Response;

public interface BackendClient {
    ResponseEntity<?> createNewUser(TelegramUser telegramUser);

    ResponseEntity<?> createAccount(AccountDTO account, long id);
    Response getCurrentBalance(long id);
    Response transfer(TransferOutgoingDTO transfer);
}
