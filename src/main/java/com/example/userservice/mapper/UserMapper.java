package com.example.userservice.mapper;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface UserMapper {

    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);

}
