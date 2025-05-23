package id.ac.ui.cs.advprog.eventsphere.payment_balance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper mapper;

    private String userId;
    private String adminId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID().toString();
        adminId = UUID.randomUUID().toString();
    }

    @Test
    void whenTopUpSuccess_thenReturnsAcceptedAndId() throws Exception {
        UUID txId = UUID.randomUUID();
        Transaction tx = mock(Transaction.class);
        when(tx.getTransactionId()).thenReturn(txId);
        when(transactionService.createTopUpTransaction(eq(userId), eq(100.0), eq("CREDIT_CARD"), anyMap()))
                .thenReturn(CompletableFuture.completedFuture(tx));

        String json = mapper.writeValueAsString(Map.of(
                "userId", userId,
                "amount", 100.0,
                "method", "CREDIT_CARD",
                "paymentData", Map.of()
        ));

        mockMvc.perform(post("/api/transactions/topup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.transactionId").value(txId.toString()));

        verify(transactionService).createTopUpTransaction(userId, 100.0, "CREDIT_CARD", anyMap());
    }

    @Test
    void whenTopUpInvalidAmount_thenReturnsBadRequest() throws Exception {
        when(transactionService.createTopUpTransaction(anyString(), eq(-50.0), anyString(), anyMap()))
                .thenReturn(CompletableFuture.failedFuture(new IllegalArgumentException("Invalid amount")));

        String json = mapper.writeValueAsString(Map.of(
                "userId", userId,
                "amount", -50.0,
                "method", "BANK_TRANSFER"
        ));

        mockMvc.perform(post("/api/transactions/topup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPurchaseSuccess_thenReturnsAcceptedAndId() throws Exception {
        UUID txId = UUID.randomUUID();
        Transaction tx = mock(Transaction.class);
        when(tx.getTransactionId()).thenReturn(txId);
        when(transactionService.createTicketPurchaseTransaction(eq(userId), eq(75.0), anyMap()))
                .thenReturn(CompletableFuture.completedFuture(tx));

        String json = mapper.writeValueAsString(Map.of(
                "userId", userId,
                "amount", 75.0,
                "ticketData", Map.of("VIP", "1")
        ));

        mockMvc.perform(post("/api/transactions/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.transactionId").value(txId.toString()));

        verify(transactionService).createTicketPurchaseTransaction(userId, 75.0, anyMap());
    }

    @Test
    void whenPurchaseInsufficient_thenReturnsBadRequest() throws Exception {
        when(transactionService.createTicketPurchaseTransaction(anyString(), anyDouble(), anyMap()))
                .thenReturn(CompletableFuture.failedFuture(new IllegalStateException("Insufficient balance")));

        String json = mapper.writeValueAsString(Map.of(
                "userId", userId,
                "amount", 500.0,
                "ticketData", Map.of("VIP", "1")
        ));

        mockMvc.perform(post("/api/transactions/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetByIdFound_thenReturnsOk() throws Exception {
        UUID txId = UUID.randomUUID();
        Transaction tx = mock(Transaction.class);
        when(tx.getTransactionId()).thenReturn(txId);
        when(transactionService.getTransactionById(eq(txId.toString()), eq(userId), eq(false)))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(tx)));

        mockMvc.perform(get("/api/transactions/{id}", txId.toString())
                        .param("currentUserId", userId)
                        .param("isAdmin", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(txId.toString()));
    }

    @Test
    void whenGetByIdNotFound_thenReturnsNotFound() throws Exception {
        String txId = UUID.randomUUID().toString();
        when(transactionService.getTransactionById(eq(txId), eq(userId), eq(false)))
                .thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        mockMvc.perform(get("/api/transactions/{id}", txId)
                        .param("currentUserId", userId)
                        .param("isAdmin", "false"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenList_thenReturnsOk() throws Exception {
        when(transactionService.filterTransactions(eq(userId), eq(false), any(), any(), any(), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));

        mockMvc.perform(get("/api/transactions")
                        .param("currentUserId", userId)
                        .param("isAdmin", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void whenDelete_thenReturnsNoContent() throws Exception {
        String txId = UUID.randomUUID().toString();
        doNothing().when(transactionService).deleteTransaction(eq(txId), eq(true));

        mockMvc.perform(delete("/api/transactions/{id}", txId)
                        .param("currentUserId", adminId)
                        .param("isAdmin", "true"))
                .andExpect(status().isNoContent());

        verify(transactionService).deleteTransaction(txId, true);
    }
}
