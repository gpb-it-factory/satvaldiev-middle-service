package ru.satvaldiev.middleservice.dto;

public class TransferIncomingDTO {
    private long id;
    private String from;
    private String to;
    private String amount;

    public TransferIncomingDTO(long id, String from, String to, String amount) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public TransferIncomingDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
