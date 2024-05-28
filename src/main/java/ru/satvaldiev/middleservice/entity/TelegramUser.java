package ru.satvaldiev.middleservice.entity;


public class TelegramUser {
    private long userId;

    public TelegramUser(long userId) {
        this.userId = userId;
    }
    public TelegramUser() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
