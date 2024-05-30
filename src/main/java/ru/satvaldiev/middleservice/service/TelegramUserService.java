package ru.satvaldiev.middleservice.service;

import ru.satvaldiev.middleservice.entity.TelegramUser;
import ru.satvaldiev.middleservice.response.Response;

public interface TelegramUserService {
     Response createNewUser(TelegramUser telegramUser);
}
