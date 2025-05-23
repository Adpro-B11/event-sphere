package id.ac.ui.cs.advprog.eventsphere.payment_balance.controller;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/topup")
    public CompletableFuture<ResponseEntity<Transaction>> topUpBalance(
            @RequestParam String userId,
            @RequestParam double amount,
            @RequestParam String method,
            @RequestBody(required = false) Map<String, String> paymentData) {
        return service.createTopUpTransaction(userId, amount, method, paymentData)
                .thenApply(tx -> ResponseEntity.status(HttpStatus.ACCEPTED).body(tx));
    }

    @PostMapping("/purchase")
    public CompletableFuture<ResponseEntity<Transaction>> purchaseTicket(
            @RequestParam String userId,
            @RequestParam double amount,
            @RequestBody Map<String, String> ticketData) {
        return service.createTicketPurchaseTransaction(userId, amount, ticketData)
                .thenApply(tx -> ResponseEntity.status(HttpStatus.ACCEPTED).body(tx));
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Transaction>> getById(
            @PathVariable String id,
            @RequestParam String currentUserId,
            @RequestParam(defaultValue = "false") boolean isAdmin) {
        service.initStrategy(isAdmin, currentUserId);
        return service.getTransactionById(id, currentUserId, isAdmin)
                .thenApply(opt -> opt
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Transaction>>> list(
            @RequestParam String currentUserId,
            @RequestParam(defaultValue = "false") boolean isAdmin,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String method,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore) {
        service.initStrategy(isAdmin, currentUserId);
        return service.filterTransactions(
                        currentUserId, isAdmin, status, type, method, createdAfter, createdBefore)
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> delete(
            @PathVariable String id,
            @RequestParam String currentUserId,
            @RequestParam(defaultValue = "false") boolean isAdmin) {
        service.initStrategy(isAdmin, currentUserId);
        service.deleteTransaction(id, isAdmin);
        return CompletableFuture.completedFuture(ResponseEntity.noContent().build());
    }
}
