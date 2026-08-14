package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Objects;

public record TransactionCustomer(String name, BigDecimal oldBalance, BigDecimal newBalance) {

    public  TransactionCustomer {
        Objects.requireNonNull(name);
        Objects.requireNonNull(oldBalance);
        Objects.requireNonNull(newBalance);
        
        if (name.trim().isEmpty()) throw new TransactionException("O nome não pode ser vazio: " + name);

        if (oldBalance.signum() < 0) throw new TransactionException("O valor de oldBalancer deve ser positivo ou zero: " + oldBalance);

        if (newBalance.signum() < 0) throw new TransactionException("O valor de newBalancer deve ser positivo ou zero: " + newBalance);
    }
}
