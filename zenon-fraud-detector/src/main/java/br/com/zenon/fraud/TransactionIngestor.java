package br.com.zenon.fraud;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TransactionIngestor {

    public static final int FRAUD_LIMIT = 10_000;

    public List<Transaction> read(String fileName) {
        Path path = Paths.get(fileName);

        try {
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
                    .limit(FRAUD_LIMIT)
                    .map(this::parseTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + fileName, e);
        }
    }

    public List<Transaction> readOldSchool(String fileName) {

        List<Transaction> transactions = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(fileName);
             Scanner sc = new Scanner(fis)) {

            int lineCount = 0;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                lineCount++;

                if (lineCount == 1) {
                    continue;
                }

                if (lineCount > 500001) {
                    break;
                }

                Optional<Transaction> transaction = parseTransaction(line);

                transaction.ifPresent(transactions::add);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + fileName, e);
        }

        return transactions;
    }

    private Optional<Transaction> parseTransaction(String line) {
        try {
            String[] chunks = line.split(",");

            int step = Integer.parseInt(chunks[0]);
            TransactionType type = TransactionType.valueOf(chunks[1]);

            if (chunks[2] == null || chunks[2].trim().isEmpty()) throw new TransactionException("O valor de amount não pode ser nulo ou vazio");
            BigDecimal amount = new BigDecimal(chunks[2]);

            var origin = new TransactionCustomer(chunks[3], new BigDecimal(chunks[4]), new BigDecimal(chunks[5]));
            var recipient = new TransactionCustomer(chunks[6], new BigDecimal(chunks[7]), new BigDecimal(chunks[8]));

            boolean isFraud = "1".equals(chunks[9]);
            boolean isFlaggedFraud = "1".equals(chunks[10]);

            return Optional.of(new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud));
        } catch (Exception e) {
            System.err.println("Erro ao fazer o parse: " + line + " | " + e.getMessage());
        }

        return Optional.empty();
    }
}
