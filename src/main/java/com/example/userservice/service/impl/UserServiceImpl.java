package com.example.userservice.service.impl;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDTO> save(UserDTO userDTO) {

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(userMapper.userDTOToUser(userDTO))
                .map(userMapper::userToUserDTO);
    }

    @Override
    public Mono<UserDTO> update(UserDTO userDTO) {

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.findById(userDTO.getId())
                .map(foundUser -> {
                    foundUser.setUsername(userDTO.getUsername());
                    foundUser.setPassword(userDTO.getPassword());
                    foundUser.setEmail(userDTO.getEmail());
                    foundUser.setRoleId(userDTO.getRoleId());
                    foundUser.setLastModifiedDate(userDTO.getLastModifiedDate());
                    foundUser.setEnabled(Boolean.valueOf(String.valueOf(userDTO.getEnabled())));
                    return foundUser;
                }).flatMap(userRepository::save)
                .map(userMapper::userToUserDTO);
    }

    @Override
    public Mono<UserDTO> patch(UserDTO userDTO) {

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.findById(userDTO.getId())
                .map(foundUser -> {

                    if (StringUtils.hasText(userDTO.getUsername()))
                        foundUser.setUsername(userDTO.getUsername());

                    if (StringUtils.hasText(userDTO.getPassword()))
                        foundUser.setPassword(userDTO.getPassword());

                    if (StringUtils.hasText(userDTO.getEmail()))
                        foundUser.setEmail(userDTO.getEmail());

                    if (userDTO.getRoleId() != null)
                        foundUser.setRoleId(userDTO.getRoleId());

                    foundUser.setLastModifiedDate(userDTO.getLastModifiedDate());
                    foundUser.setEnabled(Boolean.valueOf(String.valueOf(userDTO.getEnabled())));

                    return foundUser;
                }).flatMap(userRepository::save)
                .map(userMapper::userToUserDTO);
    }

    @Override
    public Mono<UserDTO> findById(Long id) {
        return userRepository.findById(id).map(userMapper::userToUserDTO);
    }

    @Override
    public Flux<UserDTO> findAll() {
        return userRepository.findAll().map(userMapper::userToUserDTO);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return userRepository.deleteById(id);
    }
}
