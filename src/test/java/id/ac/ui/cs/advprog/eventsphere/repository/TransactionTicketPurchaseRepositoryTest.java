package id.ac.ui.cs.advprog.eventsphere.repository;

import id.ac.ui.cs.advprog.eventsphere.enums.TransactionType;
import id.ac.ui.cs.advprog.eventsphere.enums.TransactionStatus;
import id.ac.ui.cs.advprog.eventsphere.model.TicketPurchaseTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTicketPurchaseRepositoryTest {

    TicketPurchaseTransactionRepository repository;
    List<TicketPurchaseTransaction> transactionList;

    @BeforeEach
    void setUp() {
        repository = new TicketPurchaseTransactionRepository();

        Map<String, String> tickets1 = new HashMap<>();
        tickets1.put("VIP", "2");

        Map<String, String> tickets2 = new HashMap<>();
        tickets2.put("VVIP", "1");

        TicketPurchaseTransaction trx1 = new TicketPurchaseTransaction(
                "txn-301", "user-301", TransactionType.TICKET_PURCHASE.getValue(), 200000, tickets1);
        trx1.setValidateStatus();

        TicketPurchaseTransaction trx2 = new TicketPurchaseTransaction(
                "txn-302", "user-302", TransactionType.TICKET_PURCHASE.getValue(), 150000, tickets2);
        trx2.setValidateStatus();

        transactionList = List.of(trx1, trx2);
    }

    @Test
    void testSaveAndFindById() {
        TicketPurchaseTransaction trx = transactionList.getFirst();
        repository.save(trx);

        TicketPurchaseTransaction found = repository.findById("txn-301");
        assertNotNull(found);
        assertEquals("txn-301", found.getTransactionId());
        assertEquals("user-301", found.getUserId());
        assertEquals(TransactionStatus.SUCCESS.getValue(), found.getStatus());
    }

    @Test
    void testFindByUserId() {
        transactionList.forEach(repository::save);
        List<TicketPurchaseTransaction> result = repository.findByUserId("user-302");

        assertEquals(1, result.size());
        assertEquals("txn-302", result.getFirst().getTransactionId());
    }

    @Test
    void testFindAll() {
        transactionList.forEach(repository::save);
        List<TicketPurchaseTransaction> all = repository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testFindByIdNotFound() {
        TicketPurchaseTransaction found = repository.findById("non-existent-id");
        assertNull(found);
    }

    @Test
    void testFindByStatus() {
        transactionList.forEach(repository::save);

        List<TicketPurchaseTransaction> successTransactions = repository.findByStatus(TransactionStatus.SUCCESS.getValue());

        assertEquals(2, successTransactions.size());
        assertTrue(successTransactions.stream()
                .allMatch(trx -> trx.getStatus().equals(TransactionStatus.SUCCESS.getValue())));
    }

}
