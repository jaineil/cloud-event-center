package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.Address;
import com.cmpe275.cloudeventcenter.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public long insert(Address address) {
        long addressId = addressRepository.save(address).getAddressId();
        return addressId;
    }

}
