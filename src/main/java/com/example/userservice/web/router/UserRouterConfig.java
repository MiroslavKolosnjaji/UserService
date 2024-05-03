package com.example.userservice.web.router;

import com.example.userservice.web.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author Miroslav Kološnjaji
 */
@Configuration
@RequiredArgsConstructor
public class UserRouterConfig {

    public static final String USER_PATH = "/api/user";
    public static final String USER_PATH_ID = USER_PATH +  "/{userId}";

    private final UserHandler userHandler;

    @Bean
    public RouterFunction<ServerResponse> userRouter() {
        return  route()
                .POST(USER_PATH, userHandler::createUser)
                .PUT(USER_PATH_ID, userHandler::updateUser)
                .PATCH(USER_PATH_ID, userHandler::patchUser)
                .GET(USER_PATH_ID, userHandler::getUserById)
                .GET(USER_PATH, userHandler::getAllUsers)
                .DELETE(USER_PATH_ID, userHandler::deleteUser)
                .build();
    }
}
