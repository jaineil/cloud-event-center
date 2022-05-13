package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.VirtualClock;
import com.cmpe275.cloudeventcenter.service.VirtualClockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/virtualTime")
public class VirtualClockController {
    @Autowired
    VirtualClockService virtualClockService;

    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<?> getTime() {
        VirtualClock time = virtualClockService.getVirtualClock();
        return new ResponseEntity<>(time, HttpStatus.OK);
    }
}
