package ru.satvaldiev.middleservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import ru.satvaldiev.middleservice.client.impl.BackendClientImpl;
import ru.satvaldiev.middleservice.dto.AccountDTO;
import ru.satvaldiev.middleservice.dto.TransferOutgoingDTO;
import ru.satvaldiev.middleservice.entity.Account;
import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.entity.Transfer;
import ru.satvaldiev.middleservice.response.Error;
import ru.satvaldiev.middleservice.response.Response;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;


@RestClientTest(BackendClientImpl.class)
class BackendClientImplTest {
    @Autowired
    MockRestServiceServer server;
    @Autowired
    BackendClientImpl backendClient;
    @Autowired
    ObjectMapper objectMapper;
    @Value("${restclient.url}")
    String baseUrl;
    static TelegramUser telegramUser;

    @TestConfiguration
    static class TestConfig {
        @Value("${restclient.url}")
        String baseUrl;

        @Bean
        public RestClient restClient(RestClient.Builder builder) {
            return builder
                    .baseUrl(baseUrl)
                    .build();
        }
    }
    @BeforeAll
    static void beforeAll() {
        telegramUser = new TelegramUser(123456, "UserName");
    }
    @Test
    void responseWithSuccessfulNewUserCreation() {
        ResponseEntity<?> expectedResponseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        this.server
                .expect(requestTo(baseUrl + "/v2/users"))
                .andRespond(withNoContent());

        ResponseEntity<?> responseEntityActual = backendClient.createNewUser(telegramUser);

        Assertions.assertEquals(expectedResponseEntity, responseEntityActual);
    }
    @Test
    void responseWithUnsuccessfulNewUserCreation() throws JsonProcessingException {
        Error errorExpected = new Error("Вы уже являетесь клиентом нашего банка", null, null, null);
        ResponseEntity<?> expectedResponseEntity = new ResponseEntity<>(errorExpected, HttpStatus.OK);
        this.server
                .expect(requestTo(baseUrl + "/v2/users"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(errorExpected), MediaType.APPLICATION_JSON));

        ResponseEntity<?> responseEntityActual = backendClient.createNewUser(telegramUser);
        Error errorActual = (Error) responseEntityActual.getBody();

        Assertions.assertEquals(expectedResponseEntity.getStatusCode(), responseEntityActual.getStatusCode());
        assert errorActual != null;
        Assertions.assertEquals(errorExpected.getMessage(), errorActual.getMessage());
    }
    @Test
    void responseWithSuccessfulAccountCreation() {
        AccountDTO accountDTO = new AccountDTO("Акционный");
        ResponseEntity<?> expectedResponseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        this.server
                .expect(requestTo(baseUrl + "/v2/users/"+ telegramUser.userId() + "/accounts"))
                .andRespond(withNoContent());

        ResponseEntity<?> responseEntityActual = backendClient.createAccount(accountDTO, telegramUser.userId());

        Assertions.assertEquals(expectedResponseEntity, responseEntityActual);
    }
    @Test
    void responseWithUnsuccessfulAccountCreation() throws JsonProcessingException {
        AccountDTO accountDTO = new AccountDTO("Акционный");
        Error errorExpected = new Error("Вы уже являетесь клиентом нашего банка", null, null, null);
        ResponseEntity<?> expectedResponseEntity = new ResponseEntity<>(errorExpected, HttpStatus.OK);

        this.server
                .expect(requestTo(baseUrl + "/v2/users/"+ telegramUser.userId() + "/accounts"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(errorExpected), MediaType.APPLICATION_JSON));

        ResponseEntity<?> responseEntityActual = backendClient.createAccount(accountDTO, telegramUser.userId());
        Error errorActual = (Error) responseEntityActual.getBody();

        Assertions.assertEquals(expectedResponseEntity.getStatusCode(), responseEntityActual.getStatusCode());
        assert errorActual != null;
        Assertions.assertEquals(errorExpected.getMessage(), errorActual.getMessage());
    }
    @Test
    void responseWithSuccessfulCurrentBalanceGetting() throws JsonProcessingException {
        List<Account> accountListExpected = List.of(new Account(null, null, "5000.00"));
        Response expectedResponse = new Response("5000.00");

        this.server
                .expect(requestTo(baseUrl + "/v2/users/" + telegramUser.userId() + "/accounts"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(accountListExpected), MediaType.APPLICATION_JSON));

        Response actualResponse = backendClient.getCurrentBalance(telegramUser.userId());

        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
    }
    @Test
    void responseWithUnsuccessfulCurrentBalanceGetting() {
        Response expectedResponse = new Response("Произошла непредвиденная ошибка");

        this.server
                .expect(requestTo(baseUrl + "/v2/users/" + telegramUser.userId() + "/accounts"))
                .andRespond(withBadRequest());

        Response actualResponse = backendClient.getCurrentBalance(telegramUser.userId());

        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage()
        );
    }

    @Test
    void responseWithSuccessfulTransfer() throws JsonProcessingException {
        TransferOutgoingDTO transferOutgoingDTO = new TransferOutgoingDTO(telegramUser.userName(), "Anatoliy", "500.00");
        Transfer transferResult = new Transfer(UUID.randomUUID());
        Response  expectedResponse = new Response(
                "Перевод средств выполнен успешно. ID транзакции: " + transferResult.transferId());

        this.server
                .expect(requestTo(baseUrl + "/v2/transfers"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(transferResult), MediaType.APPLICATION_JSON));

        Response actualResponse = backendClient.transfer(transferOutgoingDTO);

        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
    }
    @Test
    void responseWithUnsuccessfulTransfer(){
        TransferOutgoingDTO transferOutgoingDTO = new TransferOutgoingDTO(telegramUser.userName(), "Anatoliy", "500.00");
        Response expectedResponse = new Response("Произошла непредвиденная ошибка");

        this.server
                .expect(requestTo(baseUrl + "/v2/transfers"))
                .andRespond(withBadRequest());

        Response actualResponse = backendClient.transfer(transferOutgoingDTO);

        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
    }
}