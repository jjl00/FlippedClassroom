package io.flippedclassroom.server.exception;

public class Http400BadRequestException extends Exception {
    public Http400BadRequestException(String message) {
        super(message);
    }
}
