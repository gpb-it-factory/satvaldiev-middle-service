package ru.satvaldiev.middleservice.controller;

import org.springframework.web.bind.annotation.*;
import ru.satvaldiev.middleservice.dto.AccountDTO;
import ru.satvaldiev.middleservice.dto.TransferIncomingDTO;
import ru.satvaldiev.middleservice.response.Response;
import ru.satvaldiev.middleservice.service.AccountService;

@RestController
@RequestMapping("/users")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{id}/accounts")
    public Response createAccount(@RequestBody AccountDTO account, @PathVariable long id) {
        return accountService.createAccount(account, id);
    }
    @GetMapping("/{id}/accounts")
    public Response getCurrentBalance(@PathVariable long id) {
        return accountService.getCurrentBalance(id);
    }
    @PostMapping("/transfers")
    public Response transfer(@RequestBody TransferIncomingDTO transfer) {
        return accountService.transfer(transfer);
    }
}
