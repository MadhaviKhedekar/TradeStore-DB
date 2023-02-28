package com.example.tradestorage.exceptionHandler;

public class InvalidTradeException extends RuntimeException {
    private final String id;
    private final String reason;

    public InvalidTradeException(final String id, final String reason) {
        super("Invalid Trade: " + id + " due to "+reason);
        this.id = id;
        this.reason=reason;
    }

    public String getId() {
        return id;
    }


    public String getReason() {
        return reason;
    }
}
