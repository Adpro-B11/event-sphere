package id.ac.ui.cs.advprog.eventsphere.payment_balance.controller;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionType;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.service.AccessStrategy;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.service.AdminAccessStrategy;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.service.TransactionService;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.service.UserAccessStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final AdminAccessStrategy adminAccessStrategy;
    private final UserAccessStrategy userAccessStrategy;

    public TransactionController(TransactionService transactionService,
                                 AdminAccessStrategy adminAccessStrategy,
                                 UserAccessStrategy userAccessStrategy) {
        this.transactionService = transactionService;
        this.adminAccessStrategy = adminAccessStrategy;
        this.userAccessStrategy = userAccessStrategy;
    }

    @PostMapping("/topup")
    public ResponseEntity<?> topUpBalance(
            @RequestHeader("User-Id") String userId,
            @RequestBody Map<String, Object> requestBody) {
        // TODO: parse amount, method, additional data
        // TODO: set strategy and call transactionService.createTransaction(...)
        return ResponseEntity.status(201).body(Map.of());
    }

    @PostMapping("/purchase-ticket")
    public ResponseEntity<?> purchaseTicket(
            @RequestHeader("User-Id") String userId,
            @RequestBody Map<String, Object> requestBody) {
        // TODO: parse amount, eventId, additional data
        // TODO: set strategy and call transactionService.createTransaction(...)
        return ResponseEntity.status(201).body(Map.of());
    }

    @GetMapping("/user")
    public ResponseEntity<List<Transaction>> getUserTransactions(
            @RequestHeader("User-Id") String userId) {
        // TODO: set strategy and return transactionService.viewUserTransactions(userId)
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/user/filter")
    public ResponseEntity<List<Transaction>> filterUserTransactionsByType(
            @RequestHeader("User-Id") String userId,
            @RequestParam(required = false) String type) {
        // TODO: set strategy and return transactionService.filterTransactionsByType(userId, type)
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions(
            @RequestHeader("User-Id") String userId) {
        // TODO: verify admin, set strategy, return transactionService.viewAllTransactions()
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Transaction>> filterTransactions(
            @RequestHeader("User-Id") String adminId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        // TODO: verify admin, set strategy, return transactionService.filterTransactions(userId, status, type)
        return ResponseEntity.ok(List.of());
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<?> deleteTransaction(
            @RequestHeader("User-Id") String userId,
            @PathVariable String transactionId) {
        // TODO: verify admin, set strategy, call transactionService.deleteTransaction(transactionId)
        return ResponseEntity.ok(Map.of("success", true));
    }

    // Helper methods
    private boolean isAdmin(String userId) {
        // TODO: implement admin check
        return false;
    }

    private void setAppropriateStrategy(String userId) {
        // TODO: choose adminAccessStrategy or userAccessStrategy and set on transactionService
    }
}
