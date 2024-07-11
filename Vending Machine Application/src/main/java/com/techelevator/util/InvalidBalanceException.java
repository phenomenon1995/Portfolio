package com.techelevator.util;

public class InvalidBalanceException extends Exception{
    public InvalidBalanceException(String errorMessage) {
        super(errorMessage);
    }
}
