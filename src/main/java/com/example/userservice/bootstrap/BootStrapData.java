package com.example.userservice.bootstrap;

import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Miroslav Kološnjaji
 */

@Component
@AllArgsConstructor
public class BootStrapData implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void run(String... args) throws Exception {
            userRepository.deleteAll().doOnSuccess(success -> loadUserData()).subscribe();
    }

    private void loadUserData(){
        userRepository.count().subscribe(count -> {
            User user1 = createUser("admin", "admin","admin@example.com", 1L, Boolean.TRUE);
            User user2 = createUser("test", "test","test@example.com", 2L, Boolean.TRUE);
            User user3 = createUser("user", "user","user@example.com", 1L, Boolean.FALSE);

            userService.save(userMapper.userToUserDTO(user1)).subscribe();
            userService.save(userMapper.userToUserDTO(user2)).subscribe();
            userService.save(userMapper.userToUserDTO(user3)).subscribe();
        });
    }

    private User createUser(String username, String password, String email, Long roleId, Boolean enabled){

        Set<Long> roles = new HashSet<>();
       roles.add(roleId);

        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .roleId(roleId)
                .enabled(enabled)
                .build();
    }
}
