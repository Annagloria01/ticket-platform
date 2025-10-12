package com.esercizio.milestone.ticket_platform.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.esercizio.milestone.ticket_platform.model.Ticket;
import com.esercizio.milestone.ticket_platform.repository.CategoryRepository;
import com.esercizio.milestone.ticket_platform.repository.TicketRepository;
import com.esercizio.milestone.ticket_platform.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("")
public class RestApiController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String getHome(Model model) {
        long totalTickets = ticketRepository.count();
        long totalTicketsCompleted = ticketRepository.findTicketByStatus(Ticket.TicketStatus.COMPLETED).size();
        long totalTicketsInProgress = ticketRepository.findTicketByStatus(Ticket.TicketStatus.IN_PROGRESS).size();
        long totalTicketsToDo = ticketRepository.findTicketByStatus(Ticket.TicketStatus.TO_DO).size();

        model.addAttribute("totalTickets", totalTickets);
        model.addAttribute("completedTickets", totalTicketsCompleted);
        model.addAttribute("inProgressTickets", totalTicketsInProgress);
        model.addAttribute("toDoTickets", totalTicketsToDo);
        return "index";
    }

    @GetMapping("/tickets")
    public String getTickets(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        if (keyword != null) {
            model.addAttribute("ticketList", ticketRepository.findByTitleContainingIgnoreCase(keyword));
        }
        model.addAttribute("ticketList", ticketRepository.findAll());
        return "tickets/displayTickets";
    }

    @GetMapping("/tickets/create")
    public String getTicketsForm(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("statusNamesList", Ticket.TicketStatus.values());
        model.addAttribute("operators", userRepository.findByRoles_Name("OPERATOR"));
        model.addAttribute("ticket", new Ticket());
        return "tickets/ticketsForm";
    }

    @PostMapping("/tickets/create")
    public String createTicket(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        Optional<Ticket> optTicket = ticketRepository.findByTitle(formTicket.getTitle());
        if (optTicket.isPresent()) {
            bindingResult.addError(new ObjectError("title", "There's already a ticket for this problem!"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("statusNamesList", Ticket.TicketStatus.values());
            model.addAttribute("operators", userRepository.findByRoles_Name("OPERATOR"));
            return "tickets/ticketsForm";
        }

        formTicket.setCreationDate(Instant.now());
        ticketRepository.save(formTicket);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket created successfully");
        return "redirect:/tickets";
    }

}
