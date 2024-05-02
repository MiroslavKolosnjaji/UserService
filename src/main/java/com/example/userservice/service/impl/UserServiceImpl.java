package com.example.userservice.service.impl;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Mono<UserDTO> save(UserDTO userDTO) {
        return userRepository.save(userMapper.userDTOToUser(userDTO))
                .map(userMapper::userToUserDTO);
    }

    @Override
    public Mono<UserDTO> update(UserDTO userDTO) {
        return userRepository.findById(userDTO.getId())
                .map(foundUser -> {
                    foundUser.setUsername(userDTO.getUsername());
                    foundUser.setPassword(userDTO.getPassword());
                    foundUser.setEmail(userDTO.getEmail());
                    foundUser.setRoles(userDTO.getRoles());
                    foundUser.setUpdated(userDTO.getUpdated());
                    foundUser.setEnabled(userDTO.getEnabled());
                    return foundUser;
                }).flatMap(userRepository::save)
                .map(userMapper::userToUserDTO);
    }

    @Override
    public Mono<UserDTO> patch(UserDTO userDTO) {
        return userRepository.findById(userDTO.getId())
                .map(foundUser -> {

                    if (StringUtils.hasText(userDTO.getUsername()))
                        foundUser.setUsername(userDTO.getUsername());

                    if (StringUtils.hasText(userDTO.getPassword()))
                        foundUser.setPassword(userDTO.getPassword());

                    if (StringUtils.hasText(userDTO.getEmail()))
                        foundUser.setEmail(userDTO.getEmail());

                    if (userDTO.getRoles() != null)
                        foundUser.setRoles(userDTO.getRoles());

                    foundUser.setUpdated(userDTO.getUpdated());
                    foundUser.setEnabled(userDTO.getEnabled());

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
