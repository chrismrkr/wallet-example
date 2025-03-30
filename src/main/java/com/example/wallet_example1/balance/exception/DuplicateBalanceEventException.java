package com.example.wallet_example1.balance.exception;

public class DuplicateBalanceEventException extends RuntimeException {
    public DuplicateBalanceEventException() {
        super();
    }

    public DuplicateBalanceEventException(String message) {
        super(message);
    }
}
