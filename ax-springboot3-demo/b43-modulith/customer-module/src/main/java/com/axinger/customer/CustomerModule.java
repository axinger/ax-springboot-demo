package com.axinger.customer;

import com.axinger.shared.CustomerId;
import org.springframework.modulith.ApplicationModule;

import java.time.LocalDateTime;

/**
 * 客户模块 - Spring Modulith 应用模块
 */
@ApplicationModule(
    displayName = "客户模块"
)
public class CustomerModule {

    /**
     * 客户聚合根
     */
    public record Customer(
            CustomerId id,
            CustomerName name,
            Email email,
            PhoneNumber phone,
            Address address,
            CustomerStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        public Customer withStatus(CustomerStatus newStatus) {
            return new Customer(
                    this.id,
                    this.name,
                    this.email,
                    this.phone,
                    this.address,
                    newStatus,
                    this.createdAt,
                    LocalDateTime.now()
            );
        }

        public Customer withAddress(Address newAddress) {
            return new Customer(
                    this.id,
                    this.name,
                    this.email,
                    this.phone,
                    newAddress,
                    this.status,
                    this.createdAt,
                    LocalDateTime.now()
            );
        }

        public boolean isActive() {
            return this.status == CustomerStatus.ACTIVE;
        }

        public boolean canPlaceOrder() {
            return this.status == CustomerStatus.ACTIVE;
        }
    }

    /**
     * 客户名称值对象
     */
    public record CustomerName(String firstName, String lastName) {

        public CustomerName {
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalArgumentException("姓不能为空");
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                throw new IllegalArgumentException("名不能为空");
            }
        }

        public String getFullName() {
            return lastName + firstName;
        }
    }

    /**
     * 邮箱值对象
     */
    public record Email(String value) {

        public Email {
            if (value == null || value.trim().isEmpty()) {
                throw new IllegalArgumentException("邮箱不能为空");
            }
            if (!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new IllegalArgumentException("邮箱格式不正确");
            }
        }
    }

    /**
     * 电话号码值对象
     */
    public record PhoneNumber(String value) {

        public PhoneNumber {
            if (value == null || value.trim().isEmpty()) {
                throw new IllegalArgumentException("电话号码不能为空");
            }
            if (!value.matches("^1[3-9]\\d{9}$")) {
                throw new IllegalArgumentException("电话号码格式不正确");
            }
        }
    }

    /**
     * 地址值对象
     */
    public record Address(
            String province,
            String city,
            String district,
            String street,
            String detail,
            String zipCode
    ) {

        public Address {
            if (province == null || province.trim().isEmpty()) {
                throw new IllegalArgumentException("省份不能为空");
            }
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("城市不能为空");
            }
            if (detail == null || detail.trim().isEmpty()) {
                throw new IllegalArgumentException("详细地址不能为空");
            }
        }

        public String getFullAddress() {
            return province + city + district + street + detail;
        }
    }

    public enum CustomerStatus {
        PENDING,    // 待激活
        ACTIVE,     // 活跃
        INACTIVE,   // 非活跃
        SUSPENDED,  // 暂停
        BLACKLISTED // 黑名单
    }
}