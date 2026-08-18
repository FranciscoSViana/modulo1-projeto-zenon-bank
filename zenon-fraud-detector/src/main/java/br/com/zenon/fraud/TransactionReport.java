package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionReport {

    private record ReportTransaction(BigDecimal amount, boolean isFraud) {
    }

    public record Statistics(long totalTransactions, long totalFrauds, BigDecimal totalAmounts) {

        private final static Statistics ZERO = new Statistics(0, 0, BigDecimal.ZERO);

        private Statistics addReportTransaction(ReportTransaction rt) {
            return new Statistics(
                    totalTransactions + 1,
                    totalFrauds + (rt.isFraud ? 1 : 0),
                    totalAmounts.add(rt.amount)
            );
        }

        private Statistics add(Statistics other) {
            return new Statistics(totalTransactions + other.totalTransactions,
                    totalFrauds + other.totalFrauds,
                    totalAmounts.add(other.totalAmounts));
        }
    }

    public Statistics generateReport(String filename) {
        Path path = Path.of(filename);

        try (Stream<String> lines = Files.lines(path)) {

            return lines
                    .skip(1)
                    .map(this::parseReportTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .reduce(
                            Statistics.ZERO,
                            Statistics::addReportTransaction,
                            Statistics::add);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<ReportTransaction> parseReportTransaction(String line) {
        try {
            String[] chunks = line.split(",");

            if (chunks[2] == null || chunks[2].trim().isEmpty())
                throw new TransactionException("O valor de amount não pode ser nulo ou vazio");
            BigDecimal amount = new BigDecimal(chunks[2]);

            boolean isFraud = "1".equals(chunks[9]);

            return Optional.of(new ReportTransaction(amount, isFraud));
        } catch (Exception e) {
            System.err.println("Erro ao fazer o parse: " + line + " | " + e.getMessage());
        }

        return Optional.empty();
    }
}
