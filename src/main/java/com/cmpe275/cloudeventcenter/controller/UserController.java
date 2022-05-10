package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.service.UserService;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<?> saveUserInfo(
            @RequestBody Map<?,?> reqBody
    ) {
        long userId = (int) reqBody.get("userId");
        String accountTypeString = String.valueOf(reqBody.get("accountType"));
        Enum.AccountType accountType = Enum.AccountType.valueOf(accountTypeString);
        String fullName = String.valueOf(reqBody.get("fullName"));
        String screenName = String.valueOf(reqBody.get("screenName"));
        String gender = String.valueOf(reqBody.get("gender"));
        String description = String.valueOf(reqBody.get("description"));
//        long addressId = (long) reqBody.get("addressId");

        UserInfo userInfo = UserInfo.builder()
                .userId(userId)
                .accountType(accountType)
                .fullName(fullName)
                .screenName(screenName)
                .description(description)
//                .addressId(addressId)
                .gender(gender)
                .build();

        userService.insertUserInfo(userInfo);

        return new ResponseEntity<>("Inserted", HttpStatus.OK);
    }
}
