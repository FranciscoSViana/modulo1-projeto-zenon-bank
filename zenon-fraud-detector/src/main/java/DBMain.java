import br.com.zenon.fraud.ConnectionFactory;
import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionIngestor;
import br.com.zenon.fraud.TransactionSQLRepository;

import java.util.List;

public class DBMain {

    static void main(String[] args) {

        ConnectionFactory.getConnection();
        IO.println("Conexão com o BD criada!");

        var repository = new TransactionSQLRepository();
        repository.findByOriginName("C1000001")
                .ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para: C1000001"));
        repository.findByOriginName("C12345")
                .ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para: C12345"));

//        var t1 = new Transaction(1, PAYMENT,
//                new BigDecimal("9839.64"),
//                new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
//                new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
//                false, false);
//
//        repository.save(t1);

        var transactionIngestor = new TransactionIngestor();


        long startTimeSQL = System.nanoTime();
        List<Transaction> transactions = transactionIngestor.read("data/PS_20174392719_1491204439457_log.csv");
        IO.println(transactions.size());

        IO.println("Iniciando adicao das transacoes no BD...");
        transactions.forEach(repository::save);

        long endTimeSQL = System.nanoTime();
        IO.println("Tempo de inserção - SQL (ms): " + (endTimeSQL - startTimeSQL) / 1_000_000.0);

        repository.findByOriginName("C1231006815")
                .ifPresentOrElse(IO::println, () -> IO.println("Transacao nao encontrada para: C1231006815"));
    }
}
