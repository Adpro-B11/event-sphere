package id.ac.ui.cs.advprog.eventsphere.ticket.service;

import id.ac.ui.cs.advprog.eventsphere.ticket.client.PaymentServiceClient;
import id.ac.ui.cs.advprog.eventsphere.ticket.command.*;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.purchase.PaymentRequest;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.purchase.PaymentResponse;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.purchase.PurchaseTicketRequest;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.purchase.PurchaseTicketResponse;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.TicketResponse;
import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.factory.TicketCommandFactory;
import id.ac.ui.cs.advprog.eventsphere.ticket.factory.TicketFactory;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {
    private final TicketRepository repository;
    private final TicketFactory ticketFactory;
    private final TicketCommandFactory commandFactory;
    private final PaymentServiceClient paymentService;

    public TicketServiceImpl(TicketRepository repository,
                             TicketFactory ticketFactory,
                             PaymentServiceClient paymentService) {
        this.repository = repository;
        this.ticketFactory = ticketFactory;
        this.commandFactory = new TicketCommandFactory(repository);
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public Ticket createTicket(String eventId, TicketType type, double price, int quota) {
        Ticket ticket = ticketFactory.createTicket(eventId, type, price, quota);
        TicketCommand command = commandFactory.createTicketCommand(ticket);
        command.execute();
        return ticket;
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket viewTicket(String ticketId) {
        ViewTicketCommand command = commandFactory.viewTicketCommand(ticketId);
        command.execute();
        return command.getResult();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> viewTicketsByEvent(String eventId) {
        ViewEventTicketsCommand command = commandFactory.viewEventTicketsCommand(eventId);
        command.execute();
        return command.getResult();
    }

    @Override
    @Transactional
    public void updateTicket(String ticketId, Map<String, Object> updates) {
        TicketCommand command = commandFactory.updateTicketCommand(ticketId, updates);
        command.execute();
    }

    @Override
    @Transactional
    public void deleteTicket(String ticketId) {
        TicketCommand command = commandFactory.deleteTicketCommand(ticketId);
        command.execute();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public PurchaseTicketResponse purchaseTicket(PurchaseTicketRequest request) {
        Ticket ticket = viewTicket(request.getTicketId());
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket not found");
        }

        double totalAmount = ticket.getPrice() * request.getQuantity();

        PaymentRequest paymentRequest = PaymentRequest.builder()
//                .userId(String.valueOf(user.getId()))
                .ticketId(request.getTicketId())
                .eventId(ticket.getEventId())
                .amount(totalAmount)
                .quantity(request.getQuantity())
                .paymentMethod(request.getPaymentMethod())
                .build();

        PaymentResponse paymentResponse = paymentService.processPayment(paymentRequest);

        if ("SUCCESS".equals(paymentResponse.getStatus())) {
            try {
                TicketCommand purchaseCommand = commandFactory.purchaseTicketCommand(
                        request.getTicketId(),
                        request.getQuantity()
                );
                purchaseCommand.execute();
            } catch (Exception e) {
                // Payment was successful but ticket decrement failed
                // You might want to implement compensation logic here
                // For now, we'll throw an exception
                throw new RuntimeException("Payment processed but ticket update failed: " + e.getMessage());
            }
        }

        return PurchaseTicketResponse.builder()
                .transactionId(paymentResponse.getTransactionId())
                .ticketId(request.getTicketId())
                .eventId(ticket.getEventId())
                .quantity(request.getQuantity())
                .totalAmount(totalAmount)
                .paymentStatus(paymentResponse.getStatus())
                .paymentMethod(request.getPaymentMethod())
                .ticketDetails(TicketResponse.fromTicket(ticket))
                .build();
    }
}