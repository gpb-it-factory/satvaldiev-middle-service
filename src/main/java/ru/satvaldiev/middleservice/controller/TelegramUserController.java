package ru.satvaldiev.middleservice.controller;

import org.springframework.web.bind.annotation.*;
import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.response.Response;
import ru.satvaldiev.middleservice.service.TelegramUserService;


@RestController
@RequestMapping("/users")
public class TelegramUserController {
    private final TelegramUserService telegramUserService;

    public TelegramUserController(TelegramUserService telegramUserService) {
        this.telegramUserService = telegramUserService;
    }

    @PostMapping
    public Response createNewUser(@RequestBody TelegramUser telegramUser) {
        return telegramUserService.createNewUser(telegramUser);
    }
}
