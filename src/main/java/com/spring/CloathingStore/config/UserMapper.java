package com.spring.CloathingStore.config;

import com.spring.CloathingStore.dtos.Userdto;
import com.spring.CloathingStore.model.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Userdto user_to_UserDto(Users user);
}
