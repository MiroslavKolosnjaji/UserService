package com.example.userservice.repository;

import com.example.userservice.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}
