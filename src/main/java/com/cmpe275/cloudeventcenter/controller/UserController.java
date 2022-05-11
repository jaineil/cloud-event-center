package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.Address;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.service.AddressService;
import com.cmpe275.cloudeventcenter.service.UserService;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @PostMapping("/createUser")
    public ResponseEntity<?> saveUserInfo(
            @RequestBody Map<?,?> reqBody
    ) {

        String street = String.valueOf(reqBody.get("street"));
        String apartment = String.valueOf(reqBody.get("apartment"));
        String city = String.valueOf(reqBody.get("city"));
        String state = String.valueOf(reqBody.get("state"));
        String zipcode = String.valueOf(reqBody.get("zipcode"));

        Address address = Address.builder()
                    .street(street)
                    .apartment(apartment)
                    .city(city)
                    .state(state)
                    .zipcode(zipcode)
                .build();

        addressService.insert(address);

        String userId = String.valueOf(reqBody.get("userId"));
        String accountTypeString = String.valueOf(reqBody.get("accountType"));
        Enum.AccountType accountType = Enum.AccountType.valueOf(accountTypeString);
        String fullName = String.valueOf(reqBody.get("fullName"));
        String screenName = String.valueOf(reqBody.get("screenName"));
        String gender = String.valueOf(reqBody.get("gender"));
        String description = String.valueOf(reqBody.get("description"));

        UserInfo userInfo = UserInfo.builder()
                    .userId(userId)
                    .fullName(fullName)
                    .accountType(accountType)
                    .screenName(screenName)
                    .gender(gender)
                    .description(description)
                    .address(address)
                .build();

        userService.insertUserInfo(userInfo);

        return new ResponseEntity<>("Inserted user with id: " + userId, HttpStatus.OK);
    }

    @GetMapping("/checkUser/{userId}")
    @ResponseBody
    public ResponseEntity<?> getPlayer(
            @PathVariable String userId
    ) {
        boolean ifUserExists = userService.checkIfUserExists(userId);
        return new ResponseEntity<>(ifUserExists, HttpStatus.OK);
    }
}
