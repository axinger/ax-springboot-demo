package com.github.axinger.service.impl;

import com.github.axinger.config.MyTrackId;
import com.github.axinger.model.dto.UserDTO;
import com.github.axinger.service.UserService;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {


    @Override
    public UserDTO findById(Long id) {
        System.out.println("MyTrackId.getId() = " + MyTrackId.getId());
        return UserDTO.builder()
                .id(id)
                .name("jim")
                .age(10L)
                .build();
    }
}
