package com.esercizio.milestone.ticket_platform.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.esercizio.milestone.ticket_platform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.esercizio.milestone.ticket_platform.model.Ticket;
import com.esercizio.milestone.ticket_platform.repository.CategoryRepository;
import com.esercizio.milestone.ticket_platform.repository.TicketRepository;
import com.esercizio.milestone.ticket_platform.repository.UserRepository;

import jakarta.validation.Valid;

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
    public String getTickets(Model model, @RequestParam(name = "keyword", required = false) String keyword, Authentication authentication) {
        Set<Ticket> ticketToShow = new HashSet<>();
        boolean hasAuthorityOperator = getAuthorityFromAuthentication(authentication, "OPERATOR");
        boolean hasAuthorityAdmin = getAuthorityFromAuthentication(authentication, "ADMIN");

        if(hasAuthorityOperator){
            ticketToShow.addAll(ticketRepository.findByUser_Username(authentication.getName()));
        }

        if(hasAuthorityAdmin){
            ticketToShow.addAll(ticketRepository.findAll());
        }

        if (keyword != null) {
            for(Ticket ticket : ticketToShow){
                if(!ticket.getTitle().contains(keyword)){
                    ticketToShow.remove(ticket);
                }
            }
            model.addAttribute("ticketList", ticketToShow);
            return "tickets/displayTickets";
        }

        model.addAttribute("ticketList", ticketToShow);
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

    @GetMapping("/tickets/{id}")
    public String showTicket(@PathVariable Long id, Model model){
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        model.addAttribute("empty", ticketOpt.isEmpty());

        if(ticketOpt.isPresent()){
            model.addAttribute("ticket", ticketOpt.get());
        }

        return "tickets/ticketDetail";

    }

    private boolean getAuthorityFromAuthentication(Authentication authentication, String authName) {
        for(GrantedAuthority authority : authentication.getAuthorities()){
            if(authority.getAuthority().equals(authName)){
                return true;
            }
        }
        return false;
    }
}
