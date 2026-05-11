package com.axinger.customer.application;

import com.axinger.customer.CustomerModule;
import com.axinger.customer.domain.CustomerRepository;
import com.axinger.shared.CustomerId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 客户应用服务 - 处理客户业务逻辑
 */
@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CustomerService(CustomerRepository customerRepository, ApplicationEventPublisher eventPublisher) {
        this.customerRepository = customerRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 注册新客户
     */
    public CustomerModule.Customer registerCustomer(
            String firstName,
            String lastName,
            String email,
            String phone,
            CustomerModule.Address address) {

        // 验证邮箱是否已存在
        CustomerModule.Email customerEmail = new CustomerModule.Email(email);
        if (customerRepository.existsByEmail(customerEmail)) {
            throw new CustomerAlreadyExistsException("邮箱已被注册: " + email);
        }

        // 验证手机号是否已存在
        CustomerModule.PhoneNumber customerPhone = new CustomerModule.PhoneNumber(phone);
        if (customerRepository.existsByPhone(customerPhone)) {
            throw new CustomerAlreadyExistsException("手机号已被注册: " + phone);
        }

        // 创建客户
        CustomerModule.CustomerName name = new CustomerModule.CustomerName(firstName, lastName);
        CustomerId customerId = CustomerId.create();

        CustomerModule.Customer customer = new CustomerModule.Customer(
                customerId,
                name,
                customerEmail,
                customerPhone,
                address,
                CustomerModule.CustomerStatus.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        CustomerModule.Customer savedCustomer = customerRepository.save(customer);

        // 发布客户注册事件
        eventPublisher.publishEvent(new CustomerRegisteredEvent(savedCustomer.id(), savedCustomer.email().value()));

        return savedCustomer;
    }

    /**
     * 激活客户
     */
    public CustomerModule.Customer activateCustomer(CustomerId customerId) {
        CustomerModule.Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("客户未找到: " + customerId));

        if (customer.status() != CustomerModule.CustomerStatus.PENDING) {
            throw new IllegalStateException("只有待激活的客户才能激活");
        }

        CustomerModule.Customer activatedCustomer = customer.withStatus(CustomerModule.CustomerStatus.ACTIVE);
        CustomerModule.Customer savedCustomer = customerRepository.save(activatedCustomer);

        // 发布客户激活事件
        eventPublisher.publishEvent(new CustomerActivatedEvent(customerId, customer.email().value()));

        return savedCustomer;
    }

    /**
     * 更新客户地址
     */
    public CustomerModule.Customer updateCustomerAddress(CustomerId customerId, CustomerModule.Address newAddress) {
        CustomerModule.Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("客户未找到: " + customerId));

        CustomerModule.Customer updatedCustomer = customer.withAddress(newAddress);
        return customerRepository.save(updatedCustomer);
    }

    /**
     * 暂停客户
     */
    public CustomerModule.Customer suspendCustomer(CustomerId customerId) {
        CustomerModule.Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("客户未找到: " + customerId));

        if (customer.status() != CustomerModule.CustomerStatus.ACTIVE) {
            throw new IllegalStateException("只有活跃客户才能被暂停");
        }

        CustomerModule.Customer suspendedCustomer = customer.withStatus(CustomerModule.CustomerStatus.SUSPENDED);
        return customerRepository.save(suspendedCustomer);
    }

    /**
     * 恢复客户
     */
    public CustomerModule.Customer reactivateCustomer(CustomerId customerId) {
        CustomerModule.Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("客户未找到: " + customerId));

        if (customer.status() != CustomerModule.CustomerStatus.SUSPENDED) {
            throw new IllegalStateException("只有被暂停的客户才能被恢复");
        }

        CustomerModule.Customer reactivatedCustomer = customer.withStatus(CustomerModule.CustomerStatus.ACTIVE);
        return customerRepository.save(reactivatedCustomer);
    }

    /**
     * 根据ID查找客户
     */
    @Transactional(readOnly = true)
    public Optional<CustomerModule.Customer> findCustomerById(CustomerId customerId) {
        return customerRepository.findById(customerId);
    }

    /**
     * 根据邮箱查找客户
     */
    @Transactional(readOnly = true)
    public Optional<CustomerModule.Customer> findCustomerByEmail(String email) {
        return customerRepository.findByEmail(new CustomerModule.Email(email));
    }

    /**
     * 根据手机号查找客户
     */
    @Transactional(readOnly = true)
    public Optional<CustomerModule.Customer> findCustomerByPhone(String phone) {
        return customerRepository.findByPhone(new CustomerModule.PhoneNumber(phone));
    }

    /**
     * 检查客户是否可以下单
     */
    @Transactional(readOnly = true)
    public boolean canCustomerPlaceOrder(CustomerId customerId) {
        return customerRepository.findById(customerId)
                .map(CustomerModule.Customer::canPlaceOrder)
                .orElse(false);
    }

    /**
     * 获取活跃客户数量
     */
    @Transactional(readOnly = true)
    public long getActiveCustomerCount() {
        return customerRepository.countByStatus(CustomerModule.CustomerStatus.ACTIVE);
    }

    // 领域事件定义
    public record CustomerRegisteredEvent(CustomerId customerId, String email) {}
    public record CustomerActivatedEvent(CustomerId customerId, String email) {}

    // 异常类
    public static class CustomerNotFoundException extends RuntimeException {
        public CustomerNotFoundException(String message) {
            super(message);
        }
    }

    public static class CustomerAlreadyExistsException extends RuntimeException {
        public CustomerAlreadyExistsException(String message) {
            super(message);
        }
    }
}