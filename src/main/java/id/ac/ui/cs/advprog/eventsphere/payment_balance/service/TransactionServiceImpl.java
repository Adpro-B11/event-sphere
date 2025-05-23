package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionStatus;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionType;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;
import id.ac.ui.cs.advprog.eventsphere.auth.service.UserService;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.repository.TransactionRepository;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.strategy.AccessStrategy;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.strategy.AdminAccessStrategy;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.strategy.UserAccessStrategy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * TransactionService now exposes async methods for creation and reads.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final UserService userService;
    private AccessStrategy strategy;

    public TransactionServiceImpl(TransactionRepository repository,
                                  UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    @Async
    public CompletableFuture<Transaction> createTopUpTransaction(String userId,
                                                                 double amount,
                                                                 String method,
                                                                 Map<String, String> paymentData) {
        String txId = UUID.randomUUID().toString();
        Transaction tx = repository.createAndSave(
                TransactionType.TOPUP_BALANCE.getValue(),
                txId, userId, amount, method, paymentData
        );
        processTopUpAsync(tx);
        return CompletableFuture.completedFuture(tx);
    }

    @Async
    public CompletableFuture<Void> processTopUpAsync(Transaction tx) {
        boolean success = false;
        try {
            userService.addBalance(String.valueOf(tx.getUserId()), tx.getAmount());
            success = true;
        } catch (Exception ignored) {}
        tx.setStatus(success ? TransactionStatus.SUCCESS.getValue() : TransactionStatus.FAILED.getValue());
        repository.update(tx);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Transaction> createTicketPurchaseTransaction(String userId,
                                                                          double amount,
                                                                          Map<String, String> ticketData) {
        String txId = UUID.randomUUID().toString();
        Transaction tx = repository.createAndSave(
                TransactionType.TICKET_PURCHASE.getValue(),
                txId, userId, amount,
                PaymentMethod.IN_APP_BALANCE.getValue(),
                ticketData
        );
        processPurchaseAsync(tx);
        return CompletableFuture.completedFuture(tx);
    }

    @Async
    public CompletableFuture<Void> processPurchaseAsync(Transaction tx) {
        boolean success = false;
        try {
            userService.deductBalance(String.valueOf(tx.getUserId()), tx.getAmount());
            success = true;
        } catch (Exception ignored) {}
        tx.setStatus(success ? TransactionStatus.SUCCESS.getValue() : TransactionStatus.FAILED.getValue());
        repository.update(tx);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Optional<Transaction>> getTransactionById(String transactionId,
                                                                       String currentUserId,
                                                                       boolean isAdmin) {
        initStrategy(isAdmin, currentUserId);
        return CompletableFuture.completedFuture(strategy.findById(transactionId));
    }

    @Override
    @Async
    public CompletableFuture<List<Transaction>> viewAllTransactions(String currentUserId,
                                                                    boolean isAdmin) {
        initStrategy(isAdmin, currentUserId);
        return CompletableFuture.completedFuture(strategy.viewAllTransactions());
    }

    @Override
    @Async
    public CompletableFuture<List<Transaction>> filterTransactions(String currentUserId,
                                                                   boolean isAdmin,
                                                                   String status,
                                                                   String type,
                                                                   String method,
                                                                   LocalDateTime createdAfter,
                                                                   LocalDateTime createdBefore) {
        initStrategy(isAdmin, currentUserId);
        return CompletableFuture.completedFuture(
                strategy.filterTransactions(currentUserId, status, type, method, createdAfter, createdBefore)
        );
    }

    @Override
    public void deleteTransaction(String transactionId, boolean isAdmin) {
        strategy.deleteTransaction(transactionId);
    }

    @Override
    public void initStrategy(boolean isAdmin, String currentUserId) {
        this.strategy = isAdmin
                ? new AdminAccessStrategy(repository)
                : new UserAccessStrategy(repository, currentUserId);
    }
}
