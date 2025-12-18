package com.github.axinger.service;

import com.github.axinger.domain.A35UserEntity;

public interface OrgService {

    A35UserEntity getUserByName(String name);

    String getDirectLeader(String employeeId);

    String getDeptLeader(String employeeId);

    String getCompanyLeader();
}
