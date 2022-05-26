package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.Address;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.service.AddressService;
import com.cmpe275.cloudeventcenter.service.UserService;
import com.cmpe275.cloudeventcenter.utils.EmailNotifierService;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://18.144.15.109:3000")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private EmailNotifierService emailNotifierService;

    @GetMapping("/signupMail/{email}")
    @ResponseBody
    public ResponseEntity<?> triggerSignupMail(
            @PathVariable String email
    ) {

        emailNotifierService.notify(
                email,
                "Welcome to CEC",
                "Hi,\n\nYour account has been successfully created. " +
                        "You will shortly receive a mail verification link."+
                        "\n\n CEC Team"
        );
        return new ResponseEntity<>("triggerSignupMail sent to "+email,HttpStatus.OK);
    }

    @GetMapping("/verificationSuccess/{email}")
    @ResponseBody
    public ResponseEntity<?> triggerVerifiedMail(
            @PathVariable String email
    ){

        emailNotifierService.notify(
                email,
                "Email Verification Success",
                "Hi,\n\nYour account has been successfully successfully verified. \n\n CEC Team"
        );
        return new ResponseEntity<>("triggerVerifiedMail sent to "+email,HttpStatus.OK);
    }


    @PostMapping("/createUser")
    public ResponseEntity<?> saveUserInfo(
            @RequestBody Map<?,?> reqBody
    ) {

        if (
                !reqBody.containsKey("city") ||
                !reqBody.containsKey("state") ||
                !reqBody.containsKey("zipcode") ||
                !reqBody.containsKey("fullName") ||
                !reqBody.containsKey("emailId")
        ) {
            return new ResponseEntity<>("Missing compulsory params", HttpStatus.BAD_REQUEST);
        }

        String street = String.valueOf(reqBody.get("street"));
        String apartment = String.valueOf(reqBody.get("apartment"));
        String city = String.valueOf(reqBody.get("city"));
        String state = String.valueOf(reqBody.get("state"));
        String zipcode = String.valueOf(reqBody.get("zipcode"));

        if (city.isEmpty() || state.isEmpty() || zipcode.isEmpty()) {
            return new ResponseEntity<>("Null string address params", HttpStatus.BAD_REQUEST);
        }

        Address address = Address.builder()
                    .street(street)
                    .apartment(apartment)
                    .city(city)
                    .state(state)
                    .zipcode(zipcode)
                .build();

        addressService.insert(address);

        String userId = String.valueOf(reqBody.get("userId"));
        String emailId = String.valueOf(reqBody.get("emailId"));
        String accountTypeString = String.valueOf(reqBody.get("accountType"));
        Enum.AccountType accountType = Enum.AccountType.valueOf(accountTypeString);
        String fullName = String.valueOf(reqBody.get("fullName"));
        String screenName = String.valueOf(reqBody.get("screenName"));
        String gender = String.valueOf(reqBody.get("gender"));
        String description = String.valueOf(reqBody.get("description"));

        if (fullName.isEmpty() || emailId.isEmpty()) {
            return new ResponseEntity<>("Null string user params", HttpStatus.BAD_REQUEST);
        }

        if (accountType == accountType.Organization) {
            gender = null;
            String screenNameLower = screenName.toLowerCase();
            String fullNameLower = fullName.toLowerCase();
            int idx = screenNameLower.indexOf(fullNameLower);
            System.out.println(idx);
            if (idx != 0) {
                return new ResponseEntity<>("An org’s screen name must equal to its full name with optional suffix", HttpStatus.BAD_REQUEST);
            }
        }

        UserInfo userInfo = UserInfo.builder()
                    .userId(userId)
                    .emailId(emailId)
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
    public ResponseEntity<?> checkIfUserExists(
            @PathVariable String userId
    ) {
        boolean ifUserExists = userService.checkIfUserExists(userId);
        return new ResponseEntity<>(ifUserExists, HttpStatus.OK);
    }

    @GetMapping("/getUser/{userId}")
    @ResponseBody
    public ResponseEntity<?> getUserInfo(
    @PathVariable String userId
    ) {
        System.out.println(userId);
        UserInfo userInfo = userService.getUserInfo(userId);

        return new ResponseEntity<>(userInfo,HttpStatus.OK);
    }
}
