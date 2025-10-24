package com.esercizio.milestone.ticket_platform.controller;

import com.esercizio.milestone.ticket_platform.model.Ticket;
import com.esercizio.milestone.ticket_platform.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketRestApiController {
    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @GetMapping("/category/{categoryId}")
    public List<Ticket> getTicketsByCategory(@PathVariable Long categoryId) {
        return ticketRepository.findByCategory_Id(categoryId);
    }

    @GetMapping("/status/{status}")
    public List<Ticket> getTicketsByStatus(@PathVariable Ticket.TicketStatus status) {
        return ticketRepository.findByStatus(status);
    }
}
