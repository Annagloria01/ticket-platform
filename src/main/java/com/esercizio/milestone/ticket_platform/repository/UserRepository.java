package com.esercizio.milestone.ticket_platform.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.esercizio.milestone.ticket_platform.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);

}
