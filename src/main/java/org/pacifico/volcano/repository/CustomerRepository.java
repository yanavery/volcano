//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.repository;

import org.pacifico.volcano.repository.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//=================================================================================================
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Retrieve the customer with the given email address value.
     */
    Optional<Customer> findByEmail(String email);
}
