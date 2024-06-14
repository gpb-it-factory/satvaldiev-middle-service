package ru.satvaldiev.middleservice.service;

import ru.satvaldiev.middleservice.dto.AccountDTO;
import ru.satvaldiev.middleservice.response.Response;

public interface AccountService {
    Response createAccount(AccountDTO account, long id);
}
