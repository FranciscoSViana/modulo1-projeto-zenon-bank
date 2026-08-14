package br.com.zenon.fraud;

public class TransactionException extends RuntimeException {

    public TransactionException(String message) {
        super(message);
    }
}
