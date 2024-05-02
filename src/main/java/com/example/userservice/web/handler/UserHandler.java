package com.example.userservice.web.handler;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import com.example.userservice.web.router.UserRouterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * @author Miroslav Kološnjaji
 */
@Component
@RequiredArgsConstructor
public class UserHandler {

    public static final String USER_ID = "userId";
    private final UserService userService;
    private final Validator validator;

    public Mono<ServerResponse> createUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDTO.class)
                .doOnNext(this::validate)
                .flatMap(userService::save)
                .flatMap(savedUser -> ServerResponse.created(UriComponentsBuilder.fromPath(UserRouterConfig.USER_PATH_ID)
                        .buildAndExpand(savedUser.getId()).toUri())
                        .build());
    }

    public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDTO.class)
                .doOnNext(this::validate)
                .flatMap(userService::update)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(userDTO -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patchUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDTO.class)
                .doOnNext(this::validate)
                .flatMap(userService::patch)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(userDTO -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getUserById(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(userService.findById(Long.valueOf(serverRequest.pathVariable(USER_ID)))
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))),User.class);
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {
        return ServerResponse.ok().body(userService.findAll(), User.class);
    }

    public Mono<ServerResponse> deleteUser(ServerRequest serverRequest) {
        return userService.findById(Long.valueOf(serverRequest.pathVariable(USER_ID)))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(userDto -> userService.deleteById(userDto.getId()))
                .then(ServerResponse.noContent().build());
    }

    private void validate(UserDTO userDTO) {
        Errors errors = new BeanPropertyBindingResult(userDTO, "userDTO");
        validator.validate(userDTO, errors);

        if(errors.hasErrors())
            throw new ServerWebInputException(errors.toString());
    }
}
