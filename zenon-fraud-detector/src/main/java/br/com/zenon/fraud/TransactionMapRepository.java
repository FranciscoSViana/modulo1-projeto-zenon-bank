package br.com.zenon.fraud;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository {

    private final Map<String, Transaction> transcationByOriginName;

    public TransactionMapRepository(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);

        this.transcationByOriginName =
                transactions
                        .stream()
                        .collect(
                                Collectors.toMap(transaction -> transaction.origin().name(),
                                        Function.identity()));
    }

    @Override
    public Optional<Transaction> findByOriginName(String originName) {
        return Optional.ofNullable(transcationByOriginName.get(originName));
    }
}
