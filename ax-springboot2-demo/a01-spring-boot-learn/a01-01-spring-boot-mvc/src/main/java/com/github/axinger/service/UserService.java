package com.github.axinger.service;


import com.github.axinger.model.dto.UserDTO;

public interface UserService {

    UserDTO findById(Long id);
}
