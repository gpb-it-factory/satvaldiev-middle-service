package ru.satvaldiev.middleservice.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.satvaldiev.middleservice.client.BackendClient;
import ru.satvaldiev.middleservice.dto.AccountDTO;
import ru.satvaldiev.middleservice.dto.TransferIncomingDTO;
import ru.satvaldiev.middleservice.dto.TransferOutgoingDTO;
import ru.satvaldiev.middleservice.mapper.TransferMapper;
import ru.satvaldiev.middleservice.response.Error;
import ru.satvaldiev.middleservice.response.Response;
import ru.satvaldiev.middleservice.service.AccountService;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {
    private final BackendClient backendClient;
    private final TransferMapper transferMapper;

    public AccountServiceImpl(BackendClient backendClient, TransferMapper transferMapper) {
        this.backendClient = backendClient;
        this.transferMapper = transferMapper;
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
    @Override
    public Response getCurrentBalance(long id) {
        return backendClient.getCurrentBalance(id);
    }

    @Override
    public Response transfer(TransferIncomingDTO transferIncomingDTO) {
        Response currentBalance = getCurrentBalance(transferIncomingDTO.getId());
        BigDecimal currentAmount = new BigDecimal(currentBalance.getMessage());
        BigDecimal transferAmount = null;
        if (transferIncomingDTO.getAmount().matches("\\d+\\.?\\d{0,2}")) {
            transferAmount = new BigDecimal(transferIncomingDTO.getAmount());
        }
        if (transferAmount == null) {
            return new Response("Сумма введена некорректно");
        }
        if (currentAmount.compareTo(transferAmount) < 0) {
            return new Response("Недостаточно средств на счете");
        }
        TransferOutgoingDTO transferOutgoingDTO = transferMapper.transferIncomingDTOtoTransferOutgoingDTO(transferIncomingDTO);

        return backendClient.transfer(transferOutgoingDTO);
    }
}
