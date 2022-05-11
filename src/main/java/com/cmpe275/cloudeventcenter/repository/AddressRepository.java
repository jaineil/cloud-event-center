package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

}
