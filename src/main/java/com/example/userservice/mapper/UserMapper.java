package com.example.userservice.mapper;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface UserMapper {

    @Mapping(target = "enabled", source = "enabled", qualifiedByName = "mapBooleanToInteger" )
    UserDTO userToUserDTO(User user);

    @Mapping(target = "enabled", source = "enabled", qualifiedByName = "mapIntegerToBoolean" )
    User userDTOToUser(UserDTO userDTO);

    @Named("mapBooleanToInteger")
    default Integer mapBooleanToInteger(Boolean value) {
        return value != null && value ? 1 : 0;
    }
    @Named("mapIntegerToBoolean")
    default Boolean mapIntegerToBoolean(Integer value) {
        return value != null && value == 1;
    }


}
