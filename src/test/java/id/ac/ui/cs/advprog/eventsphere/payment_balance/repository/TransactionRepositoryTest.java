package id.ac.ui.cs.advprog.eventsphere.payment_balance.repository;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionStatus;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionType;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.TicketPurchaseTransaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.TopUpTransaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionRepositoryTest {

    private TransactionRepository repository;
    private List<Transaction> transactionList;
    private String userId1;
    private String userId2;

    @BeforeEach
    void setUp() {
        repository = new TransactionRepository();
        userId1 = UUID.randomUUID().toString();
        userId2 = UUID.randomUUID().toString();

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("accountNumber", "6631286683123456");

        Map<String, String> ticketData = new HashMap<>();
        ticketData.put("VIP", "2");
        ticketData.put("REGULAR", "5");
        ticketData.put("PREMIUM", "1");

        TicketPurchaseTransaction trx1 = new TicketPurchaseTransaction(
                "trx-001",
                userId1,
                TransactionType.TICKET_PURCHASE.getValue(),
                100000,
                PaymentMethod.IN_APP_BALANCE.getValue(),
                ticketData
        );
        TopUpTransaction trx2 = new TopUpTransaction(
                "trx-002",
                userId2,
                TransactionType.TOPUP_BALANCE.getValue(),
                50000,
                PaymentMethod.CREDIT_CARD.getValue(),
                paymentData2
        );

        trx1.setStatus(TransactionStatus.SUCCESS.getValue());
        trx2.setStatus(TransactionStatus.SUCCESS.getValue());

        transactionList = List.of(trx1, trx2);
    }

    @Test
    void testCreateAndSaveTicketPurchase() {
        Map<String, String> ticketData = Map.of(
                "VIP", "3",
                "REGULAR", "10"
        );
        String newUser = UUID.randomUUID().toString();

        Transaction tx = repository.createAndSave(
                TransactionType.TICKET_PURCHASE.getValue(),
                "trx-003",
                newUser,
                150000,
                PaymentMethod.IN_APP_BALANCE.getValue(),
                ticketData
        );

        assertNotNull(tx);
        assertEquals("trx-003", tx.getTransactionId());
        assertEquals(newUser, tx.getUserId());
        assertInstanceOf(TicketPurchaseTransaction.class, tx);

        TicketPurchaseTransaction tpt = (TicketPurchaseTransaction) tx;
        assertEquals("3", tpt.getTicketData().get("VIP"));
        assertEquals("10", tpt.getTicketData().get("REGULAR"));
        assertEquals(TransactionStatus.PENDING.getValue(), tx.getStatus());
    }

    @Test
    void testCreateAndSaveTopUp() {
        Map<String, String> paymentData = Map.of(
                "bankName", "Bank BCA",
                "accountNumber", "1234567890"
        );
        String newUser = UUID.randomUUID().toString();

        Transaction tx = repository.createAndSave(
                TransactionType.TOPUP_BALANCE.getValue(),
                "trx-004",
                newUser,
                75000,
                PaymentMethod.BANK_TRANSFER.getValue(),
                paymentData
        );

        assertNotNull(tx);
        assertEquals("trx-004", tx.getTransactionId());
        assertEquals(newUser, tx.getUserId());
        assertInstanceOf(TopUpTransaction.class, tx);

        TopUpTransaction tut = (TopUpTransaction) tx;
        assertEquals(PaymentMethod.BANK_TRANSFER.getValue(), tut.getMethod());
        assertEquals("Bank BCA", tut.getPaymentData().get("bankName"));
        assertEquals("1234567890", tut.getPaymentData().get("accountNumber"));
        assertEquals(TransactionStatus.PENDING.getValue(), tx.getStatus());
    }

    @Test
    void testSaveAndFindById() {
        transactionList.forEach(repository::save);
        Optional<Transaction> found = repository.findById("trx-001");

        assertTrue(found.isPresent());
        Transaction tx = found.get();
        assertEquals("trx-001", tx.getTransactionId());
        assertEquals(userId1, tx.getUserId());
        assertInstanceOf(TicketPurchaseTransaction.class, tx);

        TicketPurchaseTransaction tpt = (TicketPurchaseTransaction) tx;
        assertEquals("2", tpt.getTicketData().get("VIP"));
        assertEquals("5", tpt.getTicketData().get("REGULAR"));
        assertEquals("1", tpt.getTicketData().get("PREMIUM"));
    }

    @Test
    void testFindByUserId() {
        transactionList.forEach(repository::save);
        List<Transaction> result = repository.findByFilters(userId2, null, null);

        assertEquals(1, result.size());
        assertEquals("trx-002", result.getFirst().getTransactionId());
    }

    @Test
    void testFindAll() {
        transactionList.forEach(repository::save);
        List<Transaction> all = repository.findAll();

        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(t -> t.getTransactionId().equals("trx-001")));
        assertTrue(all.stream().anyMatch(t -> t.getTransactionId().equals("trx-002")));
    }

    @Test
    void testFindByStatus() {
        transactionList.forEach(repository::save);
        List<Transaction> list = repository.findByFilters(null, TransactionStatus.SUCCESS.getValue(), null);

        assertEquals(2, list.size());
        assertTrue(list.stream().allMatch(t -> t.getStatus().equals(TransactionStatus.SUCCESS.getValue())));
    }

    @Test
    void testFindByType() {
        transactionList.forEach(repository::save);
        List<Transaction> list = repository.findByFilters(null, null, TransactionType.TICKET_PURCHASE.getValue());

        assertEquals(1, list.size());
        assertEquals("trx-001", list.getFirst().getTransactionId());
    }

    @Test
    void testFindByMultipleFilters() {
        transactionList.forEach(repository::save);
        List<Transaction> list = repository.findByFilters(
                userId1,
                TransactionStatus.SUCCESS.getValue(),
                TransactionType.TICKET_PURCHASE.getValue()
        );

        assertEquals(1, list.size());
        assertEquals("trx-001", list.getFirst().getTransactionId());
    }

    @Test
    void testDeleteById() {
        transactionList.forEach(repository::save);
        repository.deleteById("trx-001");

        assertTrue(repository.findById("trx-001").isEmpty());
        assertEquals(1, repository.findAll().size());
        assertEquals("trx-002", repository.findAll().getFirst().getTransactionId());
    }

    @Test
    void testCreateAndSaveWithInvalidType() {
        assertThrows(IllegalArgumentException.class, () ->
                repository.createAndSave(
                        "INVALID",
                        "trx-009",
                        UUID.randomUUID().toString(),
                        100000,
                        PaymentMethod.BANK_TRANSFER.getValue(),
                        Map.of("bankName", "BCA")
                )
        );
    }

    @Test
    void testSaveNullTransaction() {
        assertThrows(NullPointerException.class, () -> repository.save(null));
    }

    @Test
    void testSaveTransactionWithNullId() {
        Map<String, String> ticketData = Map.of("VIP", "1");
        TicketPurchaseTransaction trx = new TicketPurchaseTransaction(
                null,
                UUID.randomUUID().toString(),
                TransactionType.TICKET_PURCHASE.getValue(),
                100000,
                PaymentMethod.IN_APP_BALANCE.getValue(),
                ticketData
        );
        assertThrows(NullPointerException.class, () -> repository.save(trx));
    }

    @Test
    void testFindByIdNotFound() {
        assertTrue(repository.findById("no-id").isEmpty());
    }

    @Test
    void testFindByUserIdNotFound() {
        assertTrue(repository.findByFilters("unknown", null, null).isEmpty());
    }

    @Test
    void testFindByStatusNotFound() {
        Map<String, String> ticketData = Map.of("REGULAR", "3");
        TicketPurchaseTransaction trx = new TicketPurchaseTransaction(
                "trx-010",
                UUID.randomUUID().toString(),
                TransactionType.TICKET_PURCHASE.getValue(),
                50000,
                PaymentMethod.IN_APP_BALANCE.getValue(),
                ticketData
        );
        repository.save(trx);
        assertTrue(repository.findByFilters(null, TransactionStatus.FAILED.getValue(), null).isEmpty());
    }

    @Test
    void testFindByTypeNotFound() {
        Map<String, String> paymentData = Map.of("accountNumber", "123", "bankName", "BCA");
        TopUpTransaction trx = new TopUpTransaction(
                "trx-011",
                UUID.randomUUID().toString(),
                TransactionType.TOPUP_BALANCE.getValue(),
                50000,
                PaymentMethod.BANK_TRANSFER.getValue(),
                paymentData
        );
        repository.save(trx);
        assertTrue(repository.findByFilters(null, null, TransactionType.TICKET_PURCHASE.getValue()).isEmpty());
    }

    @Test
    void testFindAllTicketPurchasesWhenNoneExist() {
        Map<String, String> paymentData = Map.of("accountNumber", "123", "bankName", "BCA");
        TopUpTransaction trx = new TopUpTransaction(
                "trx-012",
                UUID.randomUUID().toString(),
                TransactionType.TOPUP_BALANCE.getValue(),
                50000,
                PaymentMethod.BANK_TRANSFER.getValue(),
                paymentData
        );
        repository.save(trx);
        assertTrue(repository.findByFilters(null, null, TransactionType.TICKET_PURCHASE.getValue()).isEmpty());
    }

    @Test
    void testFindAllTopUpsWhenNoneExist() {
        Map<String, String> ticketData = Map.of("VIP", "1", "REGULAR", "2");
        TicketPurchaseTransaction trx = new TicketPurchaseTransaction(
                "trx-013",
                UUID.randomUUID().toString(),
                TransactionType.TICKET_PURCHASE.getValue(),
                100000,
                PaymentMethod.IN_APP_BALANCE.getValue(),
                ticketData
        );
        repository.save(trx);
        assertTrue(repository.findByFilters(null, null, TransactionType.TOPUP_BALANCE.getValue()).isEmpty());
    }

    @Test
    void testCreateAndSaveWithInvalidData() {
        assertThrows(IllegalArgumentException.class, () ->
                repository.createAndSave(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "trx-014",
                        UUID.randomUUID().toString(),
                        100000,
                        PaymentMethod.BANK_TRANSFER.getValue(),
                        Map.of("accountNumber", "123")
                )
        );
    }

    @Test
    void testTicketPurchaseWithMultipleTicketTypes() {
        Map<String, String> ticketData = Map.of(
                "VIP", "2",
                "REGULAR", "10",
                "ECONOMY", "5",
                "BACKSTAGE", "1"
        );
        String newUser = UUID.randomUUID().toString();
        Transaction tx = repository.createAndSave(
                TransactionType.TICKET_PURCHASE.getValue(),
                "trx-015",
                newUser,
                200000,
                PaymentMethod.IN_APP_BALANCE.getValue(),
                ticketData
        );
        assertNotNull(tx);
        assertInstanceOf(TicketPurchaseTransaction.class, tx);
        TicketPurchaseTransaction tpt = (TicketPurchaseTransaction) tx;
        assertEquals("2", tpt.getTicketData().get("VIP"));
        assertEquals("10", tpt.getTicketData().get("REGULAR"));
        assertEquals("5", tpt.getTicketData().get("ECONOMY"));
        assertEquals("1", tpt.getTicketData().get("BACKSTAGE"));
    }

    @Test
    void testTicketPurchaseWithEmptyTicketData() {
        Map<String, String> ticketData = Collections.emptyMap();
        assertThrows(IllegalArgumentException.class, () ->
                repository.createAndSave(
                        TransactionType.TICKET_PURCHASE.getValue(),
                        "trx-016",
                        UUID.randomUUID().toString(),
                        0,
                        PaymentMethod.IN_APP_BALANCE.getValue(),
                        ticketData
                )
        );
    }

    @Test
    void testTicketPurchaseWithNumericQuantities() {
        Map<String, String> ticketData = Map.of("VIP", "2", "REGULAR", "3");
        String newUser = UUID.randomUUID().toString();
        TicketPurchaseTransaction trx = new TicketPurchaseTransaction(
                "trx-017",
                newUser,
                TransactionType.TICKET_PURCHASE.getValue(),
                150000,
                PaymentMethod.IN_APP_BALANCE.getValue(),
                ticketData
        );
        repository.save(trx);
        Transaction retrieved = repository.findById("trx-017").orElse(null);
        assertNotNull(retrieved);
        assertInstanceOf(TicketPurchaseTransaction.class, retrieved);
        TicketPurchaseTransaction tpt = (TicketPurchaseTransaction) retrieved;
        assertEquals("2", tpt.getTicketData().get("VIP"));
        assertEquals("3", tpt.getTicketData().get("REGULAR"));
    }
}
