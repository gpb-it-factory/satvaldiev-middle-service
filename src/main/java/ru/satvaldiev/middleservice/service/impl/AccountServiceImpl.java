package ru.satvaldiev.middleservice.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.satvaldiev.middleservice.client.BackendClient;
import ru.satvaldiev.middleservice.dto.AccountDTO;
import ru.satvaldiev.middleservice.response.Error;
import ru.satvaldiev.middleservice.response.Response;
import ru.satvaldiev.middleservice.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
    private final BackendClient backendClient;

    public AccountServiceImpl(BackendClient backendClient) {
        this.backendClient = backendClient;
    }
    @Override
    public Response createAccount(AccountDTO account, long id) {
        ResponseEntity<?> responseEntity = backendClient.createAccount(account, id);

        if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT)) {
            return new Response("Поздравляем! Вы открыли счет \"Акционный\" с 5000 рублей в подарок! " +
                    "Информируем Вас, что при смене Вами своего имени пользователя в Telegram, вы незамедлительно " +
                    "должны оповестить нас при помощи команды /updateusername");
        }
        if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)) {
            return new Response("У Вас уже есть счет в нашем банке");
        }
        Error error = (Error) responseEntity.getBody();
        if (error == null || error.getMessage() == null) {
            return new Response("Произошла непредвиденная ошибка");
        }
        return new Response(error.getMessage());
    }
}
