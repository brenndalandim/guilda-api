package br.com.guilda.exception;

import java.util.List;

public class InvalidRequestException extends RuntimeException {
    private final List<String> details;

    public InvalidRequestException(String message, List<String> details) {
        super(message);
        this.details = details;
    }

    public List<String> getDetails() {
        return details;
    }
}