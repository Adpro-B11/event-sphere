package id.ac.ui.cs.advprog.eventsphere.payment_balance.repository;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.*;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.factory.TransactionFactoryProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionRepositoryTest {

    private TransactionRepository repository;
    private TransactionFactoryProducer factoryProducer;
    private List<Transaction> transactionList;

    @BeforeEach
    void setUp() {
        factoryProducer = new TransactionFactoryProducer();
        repository = new TransactionRepository(factoryProducer);

        // Sample transaction data
        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("bankName", "Bank BCA");
        paymentData1.put("accountNumber", "6631286837");

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("accountNumber", "663128683123456");

        TicketPurchaseTransaction trx1 = new TicketPurchaseTransaction("trx-001", "user-01", "TICKET_PURCHASE", 100000, paymentData1);
        trx1.setStatus("SUCCESS");

        TopUpTransaction trx2 = new TopUpTransaction("trx-002", "user-02", "TOPUP_BALANCE", "CREDIT_CARD", 50000, paymentData2);
        trx2.setStatus("SUCCESS");

        transactionList = List.of(trx1, trx2);
    }

    @Test
    void testCreateAndSaveTransaction() {
        Transaction transaction = repository.createAndSave("TICKET_PURCHASE", "trx-001", "user-01", 100000, "BANK_TRANSFER", Map.of("bankName", "Bank BCA"));
        assertNotNull(transaction);
        assertEquals("trx-001", transaction.getTransactionId());
    }

    @Test
    void testSaveAndFindById() {
        repository.save(transactionList.get(0));
        Transaction found = repository.findById("trx-001").orElse(null);

        assertNotNull(found);
        assertEquals("trx-001", found.getTransactionId());
        assertEquals("user-01", found.getUserId());
    }

    @Test
    void testFindByUserId() {
        transactionList.forEach(repository::save);
        List<Transaction> result = repository.findByUserId("user-02");

        assertEquals(1, result.size());
        assertEquals("trx-002", result.get(0).getTransactionId());
    }

    @Test
    void testFindAll() {
        transactionList.forEach(repository::save);
        List<Transaction> all = repository.findAll();

        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(trx -> trx.getTransactionId().equals("trx-001")));
        assertTrue(all.stream().anyMatch(trx -> trx.getTransactionId().equals("trx-002")));
    }

    @Test
    void testFindByStatus() {
        transactionList.forEach(repository::save);
        List<Transaction> successList = repository.findByStatus("SUCCESS");

        assertEquals(2, successList.size());
        assertTrue(successList.stream().allMatch(trx -> trx.getStatus().equals("SUCCESS")));
    }

    @Test
    void testFindByType() {
        transactionList.forEach(repository::save);
        List<Transaction> ticketPurchaseList = repository.findByType("TICKET_PURCHASE");

        assertEquals(1, ticketPurchaseList.size());
        assertEquals("trx-001", ticketPurchaseList.get(0).getTransactionId());
    }

    @Test
    void testFindAllTicketPurchases() {
        transactionList.forEach(repository::save);
        List<TicketPurchaseTransaction> ticketPurchases = repository.findAllTicketPurchases();

        assertEquals(1, ticketPurchases.size());
        assertEquals("trx-001", ticketPurchases.get(0).getTransactionId());
    }

    @Test
    void testFindAllTopUps() {
        transactionList.forEach(repository::save);
        List<TopUpTransaction> topUps = repository.findAllTopUps();

        assertEquals(1, topUps.size());
        assertEquals("trx-002", topUps.get(0).getTransactionId());
    }

    @Test
    void testDeleteById() {
        transactionList.forEach(repository::save);
        repository.deleteById("trx-001");

        Transaction found = repository.findById("trx-001").orElse(null);
        assertNull(found);

        List<Transaction> remaining = repository.findAll();
        assertEquals(1, remaining.size());
        assertEquals("trx-002", remaining.get(0).getTransactionId());
    }
}
