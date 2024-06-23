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
import ru.satvaldiev.middleservice.dto.AccountDTO;
import ru.satvaldiev.middleservice.dto.TransferIncomingDTO;
import ru.satvaldiev.middleservice.dto.TransferOutgoingDTO;
import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.entity.Transfer;
import ru.satvaldiev.middleservice.mapper.TransferMapper;
import ru.satvaldiev.middleservice.response.Response;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private BackendClient backendClient;
    @Mock
    private TransferMapper transferMapper;

    @InjectMocks
    private AccountServiceImpl accountService;
    private AccountDTO account;
    private TelegramUser telegramUser;

    @BeforeEach
    void beforeEach() {
        account = new AccountDTO("Акционный");
        telegramUser = new TelegramUser(123456, "UserName");
    }

    @Test
    void responseTextWhenCreateAccountIsSuccessful() {
        when(backendClient.createAccount(account, telegramUser.userId()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        Response response = accountService.createAccount(account, telegramUser.userId());

        Assertions.assertEquals("Поздравляем! Вы открыли счет \"Акционный\" с 5000 рублей в подарок! " +
                "Информируем Вас, что при смене Вами своего имени пользователя в Telegram, вы незамедлительно " +
                "должны оповестить нас при помощи команды /updateusername", response.getMessage());
    }
    @Test
    void responseTextWhenAccountIsAlreadyExist() {
        when(backendClient.createAccount(account, telegramUser.userId()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONFLICT));

        Response response = accountService.createAccount(account, telegramUser.userId());

        Assertions.assertEquals("У Вас уже есть счет в нашем банке", response.getMessage());
    }
    @Test
    void responseTextWhenCreateAccountIsUnsuccessful() {
        when(backendClient.createAccount(account, telegramUser.userId()))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        Response response = accountService.createAccount(account, telegramUser.userId());

        Assertions.assertEquals("Произошла непредвиденная ошибка", response.getMessage());
    }
    @Test
    void responseTextWhenGettingCurrentBalanceIsSuccessful() {

        when(backendClient.getCurrentBalance(telegramUser.userId()))
                .thenReturn(new Response("5000.00"));

        Response response = accountService.getCurrentBalance(telegramUser.userId());

        Assertions.assertEquals("5000.00", response.getMessage());
    }

    @Test
    void responseTextWhenAmountInIncomingDTO_IsIncorrect() {
        TransferIncomingDTO transferIncomingDTO = new TransferIncomingDTO(
                telegramUser.userId(), telegramUser.userName(), "Anatoliy", "4000,00");
        when(accountService.getCurrentBalance(telegramUser.userId()))
                .thenReturn(new Response("5000.00"));
        Response expectedResponse = new Response("Сумма введена некорректно");

        Response actualResponse = accountService.transfer(transferIncomingDTO);

        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    void responseTextWhenAmountInIncomingDTO_IsInsufficient() {
        TransferIncomingDTO transferIncomingDTO = new TransferIncomingDTO(
                telegramUser.userId(), telegramUser.userName(), "Anatoliy", "4000.00");
        when(accountService.getCurrentBalance(telegramUser.userId()))
                .thenReturn(new Response("3000.00"));
        Response expectedResponse = new Response("Недостаточно средств на счете");

        Response actualResponse = accountService.transfer(transferIncomingDTO);

        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
    }
    @Test
    void responseTextWhenTransferIsSuccessful() {
        TransferIncomingDTO transferIncomingDTO = new TransferIncomingDTO(
                telegramUser.userId(), telegramUser.userName(), "Anatoliy", "4000.00");
        TransferOutgoingDTO transferOutgoingDTO = new TransferOutgoingDTO(
                transferIncomingDTO.getFrom(), transferIncomingDTO.getTo(), transferIncomingDTO.getAmount());
        Transfer expectedTransfer = new Transfer(UUID.randomUUID());
        when(accountService.getCurrentBalance(telegramUser.userId()))
                .thenReturn(new Response("5000.00"));
        when(transferMapper.transferIncomingDTOtoTransferOutgoingDTO(transferIncomingDTO))
                .thenReturn(transferOutgoingDTO);
        when(backendClient.transfer(transferOutgoingDTO))
                .thenReturn(new Response(expectedTransfer.transferId().toString()));
        Response expectedResponse = new Response(expectedTransfer.transferId().toString());

        Response actualResponse = accountService.transfer(transferIncomingDTO);

        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
    }
}