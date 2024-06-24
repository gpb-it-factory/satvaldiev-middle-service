package ru.satvaldiev.middleservice.service;

import ru.satvaldiev.middleservice.dto.AccountDTO;
import ru.satvaldiev.middleservice.dto.TransferIncomingDTO;
import ru.satvaldiev.middleservice.response.Response;

public interface AccountService {
    Response createAccount(AccountDTO account, long id);
    Response getCurrentBalance(long id);
    Response transfer(TransferIncomingDTO transfer);
}
