package com.esercizio.milestone.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.esercizio.milestone.ticket_platform.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c.name FROM Category c")
    List<String> findAllCategoryNames();
}
