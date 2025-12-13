package com.github.axinger.service.impl;

import com.github.axinger.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String add(String a, String b) {
        return a + "_" + b;
    }
}
