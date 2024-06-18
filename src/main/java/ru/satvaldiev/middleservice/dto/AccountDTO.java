package ru.satvaldiev.middleservice.dto;

public class AccountDTO {
    private String accountName;

    public AccountDTO(String accountName) {
        this.accountName = accountName;
    }

    public AccountDTO() {
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
