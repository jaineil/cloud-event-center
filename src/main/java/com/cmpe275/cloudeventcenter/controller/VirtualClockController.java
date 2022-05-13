package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.VirtualClock;
import com.cmpe275.cloudeventcenter.service.VirtualClockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("/simulate/{virtualTimeStr}")
    @ResponseBody
    public ResponseEntity<?> simulate(@PathVariable String virtualTimeStr) {
        LocalDateTime virtualTime = LocalDateTime.parse(virtualTimeStr);
        System.out.println(virtualTime);
        virtualClockService.simulate(virtualTime);
        return new ResponseEntity<>("Simulation done", HttpStatus.OK);
    }
}
