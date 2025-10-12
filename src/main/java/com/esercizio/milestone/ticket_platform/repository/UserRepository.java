package com.esercizio.milestone.ticket_platform.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.esercizio.milestone.ticket_platform.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u.username FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<String> findAllUserNamesByRoleName(@Param("roleName") String roleName);

    List<User> findByRoles_Name(String string);

}
