package com.ciliosencantados.exception;

import java.io.IOException;

public class ListEventException extends RuntimeException {
    public ListEventException(Throwable cause) {
        super(cause);
    }

    public ListEventException(String message) {
        super(message);
    }

    public ListEventException(String message, Throwable e) {
        super(message, e);
    }
}
