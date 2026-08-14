package br.com.zenon.fraud;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public record Transaction(int step, TransactionType type, BigDecimal amount,
                          TransactionCustomer origin,
                          TransactionCustomer recipient,
                          boolean isFraud, boolean isFlaggedFraud) implements Serializable {

    public Transaction {
        Objects.requireNonNull(type);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(origin);
        Objects.requireNonNull(recipient);

        if (step <= 0) throw new TransactionException(" O valor de step deve ser positivo: " + step);

        if (amount.signum() < 0) throw new TransactionException(" O valor de amount deve ser positivo ou zero: " + amount);
    }
}
