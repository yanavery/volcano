//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.service;

import org.pacifico.volcano.beans.CustomerBean;
import org.pacifico.volcano.repository.CustomerRepository;
import org.pacifico.volcano.repository.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

//=================================================================================================
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Finds the given customer based on email address, or creates it if doesn't exist.
     */
    @Transactional
    public Customer findOrCreate(CustomerBean customerBean) {
        Optional<Customer> customer = customerRepository.findByEmail(customerBean.getEmail());
        return customer.orElseGet(() -> create(customerBean));
    }

    /**
     * Creates a customer off the provided customer bean.
     */
    @Transactional
    private Customer create(CustomerBean customerBean) {
        Customer entity = new Customer();
        entity.setEmail(customerBean.getEmail());
        entity.setName((customerBean.getName()));
        return customerRepository.save(entity);
    }
}
