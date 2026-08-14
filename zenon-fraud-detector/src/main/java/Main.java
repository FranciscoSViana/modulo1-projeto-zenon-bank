import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionCustomer;

import java.math.BigDecimal;

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
    }
}
