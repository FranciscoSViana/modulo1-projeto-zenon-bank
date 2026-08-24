import br.com.zenon.fraud.EfficientTransactionIngestor;
import br.com.zenon.fraud.TransactionSQLRepository;

public class IngestionMain {

    static void main(String[] args) {

        var repository = new TransactionSQLRepository();

        var transactionIngestor = new EfficientTransactionIngestor();


        long startTimeSQL = System.nanoTime();
        transactionIngestor.readAsBatch("data/PS_20174392719_1491204439457_log.csv", repository::saveAll);

        long endTimeSQL = System.nanoTime();
        IO.println("Tempo de ingestão no BD (ms): " + (endTimeSQL - startTimeSQL) / 1_000_000.0);
    }
}
