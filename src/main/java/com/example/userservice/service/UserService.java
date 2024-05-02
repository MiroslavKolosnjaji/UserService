package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Miroslav Kološnjaji
 */
public interface UserService {

    Mono<UserDTO> save(UserDTO userDTO);
    Mono<UserDTO> update(UserDTO userDTO);
    Mono<UserDTO> patch(UserDTO userDTO);
    Mono<UserDTO> findById(Long id);
    Flux<UserDTO> findAll();
    Mono<Void> deleteById(Long id);

}
