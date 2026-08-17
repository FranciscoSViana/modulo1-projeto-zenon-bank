import br.com.zenon.fraud.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static br.com.zenon.fraud.TransactionType.CASH_OUT;
import static br.com.zenon.fraud.TransactionType.PAYMENT;


public class Main {

    static void main(String[] args) {

        var t1 = new Transaction(1, PAYMENT,
                new BigDecimal("9839.64"), new TransactionCustomer("C1231006815",
                new BigDecimal("170136.0"), new BigDecimal("160296.36")),
                new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
                false, false);

        var t2 = new Transaction(2, CASH_OUT,
                new BigDecimal("850002.52"), new TransactionCustomer("C1280323807",
                new BigDecimal("8500002.52"), new BigDecimal("0.0")),
                new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
                true, false);

        IO.println(t1);
        IO.println(t2);


        IO.println("--------------------------------------------------------------------------------------");

        var transactionIngestor = new TransactionIngestor();
        List<Transaction> transactions = transactionIngestor.read("data/PS_20174392719_1491204439457_log.csv");
        IO.println(transactions.size());

        transactions.stream().limit(10).forEach(System.out::println);

        IO.println("--------------------------------------------------------------------------------------");

        List<Transaction> transactionsBadData = transactionIngestor.read("data/paysim_with_bad_data.csv");
        IO.println(transactionsBadData.size());

        transactionsBadData.stream().limit(10).forEach(System.out::println);

        IO.println("--------------------------------------------------------------------------------------");

        var fraudAnalyzer =  new FraudAnalyzer(transactions);
        long countFrauds = fraudAnalyzer.countFrauds();
        IO.println("1. Total de fraudes: " + countFrauds);

        List<BigDecimal> highestFraudsAmounts = fraudAnalyzer.findHigestValuesFraudsAmounts(3);
        IO.println("2. Top 3 Fraudes de Maior Valor: ");
        highestFraudsAmounts.forEach(amount -> IO.println("- %.2f".formatted(amount)));

        List<String> suspiciousClients = fraudAnalyzer.findTopSuspiciousClients(5);
        IO.println("3. Clientes Suspeitos: ");
        suspiciousClients.stream().forEach(System.out::println);

        BigDecimal totalFraudLoss = fraudAnalyzer.calculateTotalFraudLoss();
        IO.println("4. Prejuízo Total: " + totalFraudLoss);


        Map<TransactionType, Long> fraudCountByType = fraudAnalyzer.countFraudsByType();
        IO.println("5. Fraudes por Tipo:");
        fraudCountByType.forEach((key, value) -> IO.println(key + " - " + value));


        IO.println("--------------------------------------------------------------------------------------");

        TransactionRepository transactionRepository;
        transactionRepository = new TransactionListRepository(transactions);

        String notFoundOriginName = "C1231";
        transactionRepository.findByOriginName(notFoundOriginName).ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada: "+ notFoundOriginName));

        String existingOriginName = "C1868032458";

        long startTimeList = System.nanoTime();
        transactionRepository.findByOriginName(existingOriginName).ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada: "+ existingOriginName));
        long endTimeList = System.nanoTime();
        IO.println("Tempo de busca - List (ms): " + (endTimeList - startTimeList) / 1_000_000.0 + " ms");

        transactionRepository = new TransactionMapRepository(transactions);
        startTimeList = System.nanoTime();
        transactionRepository.findByOriginName(existingOriginName).ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada: "+ existingOriginName));
        endTimeList = System.nanoTime();
        IO.println("Tempo de busca - Map (ms): " + (endTimeList - startTimeList) / 1_000_000.0 + " ms");
    }
}
