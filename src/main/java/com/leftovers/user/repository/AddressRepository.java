package com.leftovers.user.repository;

import com.leftovers.user.model.Address;
import com.leftovers.user.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

}
