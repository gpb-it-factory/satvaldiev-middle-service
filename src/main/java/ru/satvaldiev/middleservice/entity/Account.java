package ru.satvaldiev.middleservice.entity;

import java.util.UUID;

public record Account (UUID accountId, String accountName, String amount){}
