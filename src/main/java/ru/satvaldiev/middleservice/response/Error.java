package ru.satvaldiev.middleservice.response;

public class Error {
    private String message;
    private String type;
    private String code;
    private String trace_id;

    public Error() {}

    public Error(String message, String type, String code, String trace_id) {
        this.message = message;
        this.type = type;
        this.code = code;
        this.trace_id = trace_id;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getTrace_id() {
        return trace_id;
    }

    public void setTrace_id(String trace_id) {
        this.trace_id = trace_id;
    }
}
