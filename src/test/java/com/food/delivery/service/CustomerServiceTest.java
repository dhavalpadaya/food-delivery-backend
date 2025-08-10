package com.food.delivery.service;

import com.food.delivery.model.Customer;
import com.food.delivery.model.IdPrefix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = CustomerService.getCustomerService();
    }

    @Test
    void testAddCustomer() {
        String name = "Customer1";
        Customer customer = customerService.addCustomer(name);

        assertNotNull(customer);
        assertTrue(customer.getCustId().startsWith(IdPrefix.CUSTOMER.get()));
        assertEquals(name, customer.getCustName());
    }

    @Test
    void testRemoveCustomerSuccess() {
        Customer added = customerService.addCustomer("Customer2");
        Optional<Customer> removed = customerService.removeCustomer(added.getCustId());

        assertTrue(removed.isPresent());
        assertEquals(added.getCustId(), removed.get().getCustId());
    }

    @Test
    void testRemoveCustomerFailure() {
        Optional<Customer> result = customerService.removeCustomer("CUST_9999");
        assertFalse(result.isPresent());
    }

    @Test
    void testCustomerIdGeneration() {
        Customer c1 = customerService.addCustomer("User1");
        Customer c2 = customerService.addCustomer("User2");

        assertNotEquals(c1.getCustId(), c2.getCustId());
        assertTrue(c1.getCustId().startsWith(IdPrefix.CUSTOMER.get()));
        assertTrue(c2.getCustId().startsWith(IdPrefix.CUSTOMER.get()));
    }

    @Test
    void testAddCustomerWithMockedName() {
        Customer mockCustomer = mock(Customer.class);
        when(mockCustomer.getCustName()).thenReturn("Customer3");

        Customer added = customerService.addCustomer(mockCustomer.getCustName());

        assertEquals("Customer3", added.getCustName());
        assertTrue(added.getCustId().startsWith(IdPrefix.CUSTOMER.get()));
    }
}