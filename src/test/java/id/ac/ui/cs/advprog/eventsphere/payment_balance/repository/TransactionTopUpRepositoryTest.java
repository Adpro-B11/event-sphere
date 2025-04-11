package id.ac.ui.cs.advprog.eventsphere.payment_balance.repository;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionStatus;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.TopUpTransaction;
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

        Map<String,String> paymentData = new HashMap<>();
        paymentData.put("bankName","Bank BCA");
        paymentData.put("accountNumber","6631286837");
        TopUpTransaction trx1 = new TopUpTransaction("trx-001", "user-01", "TOPUP_BALANCE","BANK_TRANSFER", 100000,paymentData );
        trx1.setValidateStatus();

        Map<String,String> paymentData2 = new HashMap<>();
        paymentData2.put("accountNumber","663128683123456");
        TopUpTransaction trx2 = new TopUpTransaction("trx-002", "user-02", "TOPUP_BALANCE","CREDIT_CARD",50000,paymentData2 );
        trx2.setValidateStatus();

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
        assertEquals("trx-001", found.getTransactionId());
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
        assertEquals("trx-002", found.getTransactionId());
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


    @Test
    void testFindByStatusIfFound() {
        for (TopUpTransaction trx : listTopUpTransactionData) {
            topUpTransactionRepository.save(trx);
        }

        List<TopUpTransaction> successTrxList = topUpTransactionRepository.findByStatus(TransactionStatus.SUCCESS.getValue());
        assertEquals(1, successTrxList.size());

        TopUpTransaction trx = successTrxList.getFirst();
        assertEquals("trx-001", trx.getTransactionId());
        assertEquals(TransactionStatus.SUCCESS.getValue(), trx.getStatus());
    }

    @Test
    void testFindByStatusIfNotFound() {
        for (TopUpTransaction trx : listTopUpTransactionData) {
            topUpTransactionRepository.save(trx);
        }

        List<TopUpTransaction> pendingTrxList = topUpTransactionRepository.findByStatus("PENDING");
        assertTrue(pendingTrxList.isEmpty());
    }

    @Test
    void testDeleteByIdIfFound() {
        for (TopUpTransaction trx : listTopUpTransactionData) {
            topUpTransactionRepository.save(trx);
        }

        topUpTransactionRepository.deleteById("trx-001");

        TopUpTransaction found = topUpTransactionRepository.findById("trx-001");
        assertNull(found);

        List<TopUpTransaction> allTransactions = topUpTransactionRepository.findAll();
        assertEquals(1, allTransactions.size());
        assertEquals("trx-002", allTransactions.getFirst().getTransactionId());
    }

    @Test
    void testDeleteByIdIfNotFound() {
        for (TopUpTransaction trx : listTopUpTransactionData) {
            topUpTransactionRepository.save(trx);
        }

        topUpTransactionRepository.deleteById("trx-999");

        List<TopUpTransaction> allTransactions = topUpTransactionRepository.findAll();
        assertEquals(2, allTransactions.size());
    }

}
