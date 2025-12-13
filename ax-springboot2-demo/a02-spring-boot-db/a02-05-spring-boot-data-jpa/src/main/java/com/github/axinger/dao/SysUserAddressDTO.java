package com.github.axinger.dao;

import com.github.axinger.entity.SysUserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository
public interface SysUserAddressDTO extends JpaRepository<SysUserAddress, Long> {
    List<SysUserAddress> findBySysUserId(Long sysUserId);
}
