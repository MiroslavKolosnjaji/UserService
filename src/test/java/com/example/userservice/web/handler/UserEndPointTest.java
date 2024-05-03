package com.example.userservice.web.handler;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import com.example.userservice.web.router.UserRouterConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;


/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserEndPointTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testCreateUser() {

        webTestClient.post()
                .uri(UserRouterConfig.USER_PATH)
                .body(Mono.just(getTestUser(null)), User.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("/api/user/4");
    }

    @Test
    void testCreateUserBadRequest() {

        var userDTO = getTestUser(null);
        userDTO.setPassword("");

        webTestClient.post()
                .uri(UserRouterConfig.USER_PATH)
                .body(Mono.just(userDTO), User.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void testUpdateUser() {

        var userDTO = getTestUser(1L);
        userDTO.setPassword("testPassword");

        webTestClient.put()
                .uri(UserRouterConfig.USER_PATH_ID, userDTO.getId())
                .body(Mono.just(userDTO), User.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateUserBadRequest() {

        var userDTO = getTestUser(1L);
        userDTO.setPassword("");

        webTestClient.put()
                .uri(UserRouterConfig.USER_PATH_ID, userDTO.getId())
                .body(Mono.just(userDTO), User.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateMemberNotFound() {

        var userDTO = getTestUser(99L);

        webTestClient.put()
                .uri(UserRouterConfig.USER_PATH_ID, userDTO.getId())
                .body(Mono.just(userDTO), User.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(4)
    void testUserPatchIdFound() {

        var userDTO = getTestUser(1L);

        webTestClient.put()
                .uri(UserRouterConfig.USER_PATH_ID, userDTO.getId())
                .body(Mono.just(userDTO), User.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUserPatchIdNotFound() {

        var userDTO = getTestUser(99L);

        webTestClient.put()
                .uri(UserRouterConfig.USER_PATH_ID, userDTO.getId())
                .body(Mono.just(userDTO), User.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(1)
    void testGetUserByid() {

        webTestClient.get()
                .uri(UserRouterConfig.USER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody(UserDTO.class);
    }

    @Test
    void testGetUserByIdNotFound() {

        webTestClient.get()
                .uri(UserRouterConfig.USER_PATH_ID, 99)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(2)
    void testGetAllUsers() {

        webTestClient.get()
                .uri(UserRouterConfig.USER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThan(1)));
    }

    @Test
    void testDeleteUser() {
        var userDTO = getTestUser(1L);

        webTestClient.delete()
                .uri(UserRouterConfig.USER_PATH_ID, userDTO.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteUserNotFound() {
        webTestClient.delete()
                .uri(UserRouterConfig.USER_PATH_ID, 99)
                .exchange().
                expectStatus()
                .isNotFound();
    }

    private UserDTO getTestUser(Long id) {
        return UserDTO.builder()
                .id(id)
                .username("test")
                .password("password")
                .email("test@test.com")
                .roleId(1L)
                .enabled(1)
                .build();
    }
}