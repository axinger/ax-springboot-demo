package com.github.axinger.service;

public interface OrgService {

    String getDirectLeader(String employeeId);

    String getDeptLeader(String employeeId);

    String getCompanyLeader();
}
