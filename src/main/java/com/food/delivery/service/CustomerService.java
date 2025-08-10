package com.food.delivery.service;

import com.food.delivery.model.Customer;
import com.food.delivery.model.IdPrefix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final Map<String, Customer> customerMap;
    private final AtomicInteger atomicInteger;

    private static final CustomerService customerServiceObj = new CustomerService();

    private CustomerService(){
        this.customerMap = new HashMap<>();
        this.atomicInteger = new AtomicInteger(1);
        logger.info("CustomerService initialized");
    }

    public static CustomerService getCustomerService(){
        logger.debug("getCustomerService() called");
        return customerServiceObj;
    }

    public Customer addCustomer(String customerName){
        String custId = getCustId();
        Customer customer = new Customer(custId, customerName);
        customerMap.put(custId, customer);

        logger.info("Added new customer: id={}, name={}", custId, customerName);
        return customer;
    }

    public Optional<Customer> removeCustomer(String custId){
        logger.info("Attempting to remove customer with id={}", custId);

        if(customerMap.containsKey(custId)){
            Customer customer = customerMap.remove(custId);
            logger.info("Customer removed: id={}, name={}", custId, customer.getCustName());
            return Optional.of(customer);
        }
        logger.warn("removeCustomer(): No customer found with id={}", custId);
        return Optional.empty();
    }

    private String getCustId(){
        String id = IdPrefix.CUSTOMER.get() + atomicInteger.getAndIncrement();
        logger.debug("Generated new customer ID: {}", id);
        return id;
    }
}
