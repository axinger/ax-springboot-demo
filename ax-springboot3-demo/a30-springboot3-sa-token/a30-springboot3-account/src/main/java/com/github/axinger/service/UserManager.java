package com.github.axinger.service;


import com.github.axinger.model.dto.AccountUserLoginDTO;

public interface UserManager {
    String login(AccountUserLoginDTO req);
}
