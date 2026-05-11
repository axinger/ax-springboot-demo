package com.axinger.customer.web;

import com.axinger.customer.CustomerModule;
import com.axinger.customer.application.CustomerService;
import com.axinger.shared.CustomerId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 客户控制器 - REST API 端点
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 注册新客户
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse registerCustomer(@RequestBody RegisterCustomerRequest request) {
        CustomerModule.Address address = new CustomerModule.Address(
                request.province(),
                request.city(),
                request.district(),
                request.street(),
                request.detail(),
                request.zipCode()
        );

        CustomerModule.Customer customer = customerService.registerCustomer(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone(),
                address
        );

        return toCustomerResponse(customer);
    }

    /**
     * 激活客户
     */
    @PostMapping("/{customerId}/activate")
    public CustomerResponse activateCustomer(@PathVariable String customerId) {
        CustomerId id = CustomerId.fromString(customerId);
        CustomerModule.Customer customer = customerService.activateCustomer(id);
        return toCustomerResponse(customer);
    }

    /**
     * 更新客户地址
     */
    @PutMapping("/{customerId}/address")
    public CustomerResponse updateCustomerAddress(
            @PathVariable String customerId,
            @RequestBody UpdateAddressRequest request) {

        CustomerId id = CustomerId.fromString(customerId);
        CustomerModule.Address address = new CustomerModule.Address(
                request.province(),
                request.city(),
                request.district(),
                request.street(),
                request.detail(),
                request.zipCode()
        );

        CustomerModule.Customer customer = customerService.updateCustomerAddress(id, address);
        return toCustomerResponse(customer);
    }

    /**
     * 暂停客户
     */
    @PostMapping("/{customerId}/suspend")
    public CustomerResponse suspendCustomer(@PathVariable String customerId) {
        CustomerId id = CustomerId.fromString(customerId);
        CustomerModule.Customer customer = customerService.suspendCustomer(id);
        return toCustomerResponse(customer);
    }

    /**
     * 恢复客户
     */
    @PostMapping("/{customerId}/reactivate")
    public CustomerResponse reactivateCustomer(@PathVariable String customerId) {
        CustomerId id = CustomerId.fromString(customerId);
        CustomerModule.Customer customer = customerService.reactivateCustomer(id);
        return toCustomerResponse(customer);
    }

    /**
     * 获取客户详情
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable String customerId) {
        CustomerId id = CustomerId.fromString(customerId);
        Optional<CustomerModule.Customer> customer = customerService.findCustomerById(id);

        return customer.map(c -> ResponseEntity.ok(toCustomerResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据邮箱查找客户
     */
    @GetMapping("/by-email/{email}")
    public ResponseEntity<CustomerResponse> getCustomerByEmail(@PathVariable String email) {
        Optional<CustomerModule.Customer> customer = customerService.findCustomerByEmail(email);

        return customer.map(c -> ResponseEntity.ok(toCustomerResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据手机号查找客户
     */
    @GetMapping("/by-phone/{phone}")
    public ResponseEntity<CustomerResponse> getCustomerByPhone(@PathVariable String phone) {
        Optional<CustomerModule.Customer> customer = customerService.findCustomerByPhone(phone);

        return customer.map(c -> ResponseEntity.ok(toCustomerResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 检查客户是否可以下单
     */
    @GetMapping("/{customerId}/can-place-order")
    public CanPlaceOrderResponse canPlaceOrder(@PathVariable String customerId) {
        CustomerId id = CustomerId.fromString(customerId);
        boolean canPlace = customerService.canCustomerPlaceOrder(id);

        return new CanPlaceOrderResponse(customerId, canPlace);
    }

    /**
     * 获取活跃客户数量
     */
    @GetMapping("/stats/active-count")
    public ActiveCustomerCountResponse getActiveCustomerCount() {
        long count = customerService.getActiveCustomerCount();
        return new ActiveCustomerCountResponse(count);
    }

    // 响应对象转换方法
    private CustomerResponse toCustomerResponse(CustomerModule.Customer customer) {
        return new CustomerResponse(
                customer.id().toString(),
                customer.name().firstName(),
                customer.name().lastName(),
                customer.name().getFullName(),
                customer.email().value(),
                customer.phone().value(),
                toAddressResponse(customer.address()),
                customer.status().name(),
                customer.createdAt(),
                customer.updatedAt()
        );
    }

    private AddressResponse toAddressResponse(CustomerModule.Address address) {
        return new AddressResponse(
                address.province(),
                address.city(),
                address.district(),
                address.street(),
                address.detail(),
                address.zipCode(),
                address.getFullAddress()
        );
    }

    // 请求/响应对象
    public record RegisterCustomerRequest(
            String firstName,
            String lastName,
            String email,
            String phone,
            String province,
            String city,
            String district,
            String street,
            String detail,
            String zipCode
    ) {}

    public record UpdateAddressRequest(
            String province,
            String city,
            String district,
            String street,
            String detail,
            String zipCode
    ) {}

    public record CustomerResponse(
            String customerId,
            String firstName,
            String lastName,
            String fullName,
            String email,
            String phone,
            AddressResponse address,
            String status,
            java.time.LocalDateTime createdAt,
            java.time.LocalDateTime updatedAt
    ) {}

    public record AddressResponse(
            String province,
            String city,
            String district,
            String street,
            String detail,
            String zipCode,
            String fullAddress
    ) {}

    public record CanPlaceOrderResponse(String customerId, boolean canPlaceOrder) {}

    public record ActiveCustomerCountResponse(long activeCustomerCount) {}
}