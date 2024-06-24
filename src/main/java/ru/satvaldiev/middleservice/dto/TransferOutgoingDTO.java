package ru.satvaldiev.middleservice.dto;

public class TransferOutgoingDTO {
    private String from;
    private String to;
    private String amount;

    public TransferOutgoingDTO(String from, String to, String amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public TransferOutgoingDTO() {
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
