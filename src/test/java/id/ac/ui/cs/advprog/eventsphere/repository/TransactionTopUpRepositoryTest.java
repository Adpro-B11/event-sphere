package id.ac.ui.cs.advprog.eventsphere.repository;

import id.ac.ui.cs.advprog.eventsphere.enums.TransactionStatus;
import id.ac.ui.cs.advprog.eventsphere.model.TopUpTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class TransactionTopUpRepositoryTest {

    TopUpTransactionRepository topUpTransactionRepository;
    List<TopUpTransaction> listTopUpTransactionData;

    @BeforeEach
    void setUp() {
        topUpTransactionRepository = new TopUpTransactionRepository();

        TopUpTransaction trx1 = new TopUpTransaction("trx-001", "user-01", 100000, TransactionStatus.SUCCESS.getValue());
        TopUpTransaction trx2 = new TopUpTransaction("trx-002", "user-02", 50000, TransactionStatus.FAILED.getValue());

        listTopUpTransactionData = new ArrayList<>();
        listTopUpTransactionData.add(trx1);
        listTopUpTransactionData.add(trx2);
    }

    @Test
    void testSaveCreate() {
        TopUpTransaction trx = listTopUpTransactionData.getFirst();
        topUpTransactionRepository.save(trx);

        TopUpTransaction found = topUpTransactionRepository.findById("trx-001");
        assertNotNull(found);
        assertEquals("trx-001", found.getId());
        assertEquals("user-01", found.getUserId());
        assertEquals(100000, found.getAmount());
        assertEquals(TransactionStatus.SUCCESS.getValue(), found.getStatus());
    }

    @Test
    void testFindByIdIfFound() {
        for (TopUpTransaction trx : listTopUpTransactionData) {
            topUpTransactionRepository.save(trx);
        }

        TopUpTransaction found = topUpTransactionRepository.findById("trx-002");
        assertNotNull(found);
        assertEquals("trx-002", found.getId());
        assertEquals("user-02", found.getUserId());
    }

    @Test
    void testFindByIdIfNotFound() {
        for (TopUpTransaction trx : listTopUpTransactionData) {
            topUpTransactionRepository.save(trx);
        }

        TopUpTransaction found = topUpTransactionRepository.findById("trx-999");
        assertNull(found);
    }

    @Test
    void testFindAll() {
        for (TopUpTransaction trx : listTopUpTransactionData) {
            topUpTransactionRepository.save(trx);
        }

        List<TopUpTransaction> result = topUpTransactionRepository.findAll();
        assertEquals(2, result.size());
    }

    public List<TopUpTransaction> findByStatus(String status) {
        List<TopUpTransaction> filtered = new ArrayList<>();
        for (TopUpTransaction trx : topUpTransactionStorage.values()) {
            if (trx.getStatus().equalsIgnoreCase(status)) {
                filtered.add(trx);
            }
        }
        return filtered;
    }

    @Test
    void testFindByStatusIfFound() {
        for (TopUpTransaction trx : listTopUpTransactionData) {
            topUpTransactionRepository.save(trx);
        }

        List<TopUpTransaction> successTrxList = topUpTransactionRepository.findByStatus(TransactionStatus.SUCCESS.getValue());
        assertEquals(1, successTrxList.size());

        TopUpTransaction trx = successTrxList.getFirst();
        assertEquals("trx-001", trx.getId());
        assertEquals(TransactionStatus.SUCCESS.getValue(), trx.getStatus());
    }

    @Test
    void testFindByStatusIfNotFound() {
        for (TopUpTransaction trx : listTopUpTransactionData) {
            topUpTransactionRepository.save(trx);
        }

        List<TopUpTransaction> pendingTrxList = topUpTransactionRepository.findByStatus(TransactionStatus.PENDING.getValue());
        assertTrue(pendingTrxList.isEmpty());
    }
}
