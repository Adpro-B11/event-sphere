package id.ac.ui.cs.advprog.eventsphere.ticket.controller;

import id.ac.ui.cs.advprog.eventsphere.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionWebhookController {

    private final TicketService ticketService;

    @PostMapping(
            path = "/purchase-success",
            consumes = "application/json"
    )
    @ResponseStatus(HttpStatus.OK)
    public void onPurchaseSuccess(@RequestBody TransactionCallbackPayload payload) {
        log.info("Received purchase-success callback for tx={} event={}",
                payload.transactionId(), payload.eventId());
        payload.data().forEach((ticketType, qtyStr) -> {
            int qty = Integer.parseInt(qtyStr);
            ticketService.decreaseQuota(payload.eventId(), ticketType, qty);
            log.debug("Decreased quota for event={}, type={} by {}",
                    payload.eventId(), ticketType, qty);
        });
    }

    public static record TransactionCallbackPayload(
            String transactionId,
            String userId,
            String eventId,
            Map<String, String> data
    ) {}
}