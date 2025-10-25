package com.esercizio.milestone.ticket_platform.repository;

import com.esercizio.milestone.ticket_platform.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note,Long> {
}
