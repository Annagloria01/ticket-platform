package com.esercizio.milestone.ticket_platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esercizio.milestone.ticket_platform.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long>{

    public List<Ticket> findTicketByStatus(Ticket.TicketStatus ticketStatus);

    public List<Ticket> findByTitleContainingIgnoreCase(String keyword);

    public Optional<Ticket> findByTitle(String title);

}
