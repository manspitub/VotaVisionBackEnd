package com.jacaranda.manuel.VotaVision.dto;

import org.springframework.stereotype.Component;
import com.jacaranda.manuel.VotaVision.model.User;

@Component
public class UserDtoConverter {
    
    public UserDto convertUserDto(User user) {
        return new UserDto(
            user.getId(),
            user.getName(),
            user.getSurname(),
            user.getEmail(),
            user.getAge(),
            user.getCity(),
            user.getProfession(),
            user.getConfirmed(),
            user.getCreatedAt(),
            user.getRole().toString(),
            user.getReward()
        );
    }
}
