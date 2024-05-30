package ru.satvaldiev.middleservice.controller;

import org.springframework.web.bind.annotation.*;
import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.response.Response;
import ru.satvaldiev.middleservice.service.impl.TelegramUserServiceImpl;

@RestController
@RequestMapping("/users")
public class TelegramUserController {
    private final TelegramUserServiceImpl telegramUserService;

    public TelegramUserController(TelegramUserServiceImpl telegramUserService) {
        this.telegramUserService = telegramUserService;
    }

    @PostMapping
    public Response createNewUser(@RequestBody TelegramUser telegramUser) {
        return telegramUserService.createNewUser(telegramUser);
    }

}
