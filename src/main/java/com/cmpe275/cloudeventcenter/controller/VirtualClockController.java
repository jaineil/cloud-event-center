package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.VirtualClock;
import com.cmpe275.cloudeventcenter.service.VirtualClockService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://18.144.15.109:3000")
@RestController
@RequestMapping("/virtualTime")
public class VirtualClockController {
    @Autowired
    VirtualClockService virtualClockService;

    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<?> getTime() {
        LocalDateTime virtualTime = virtualClockService.getVirtualClock();
        return new ResponseEntity<>(virtualTime, HttpStatus.OK);
    }

    @GetMapping("/simulate/{virtualTimeStr}")
    @ResponseBody
    public ResponseEntity<?> simulate(@PathVariable String virtualTimeStr) {
        LocalDateTime newVirtualTime = LocalDateTime.parse(virtualTimeStr);
        if(!virtualClockService.isValidSimulationTime(newVirtualTime)) {
            return new ResponseEntity<>("Not a valid future time, only future time up to 1 year allowed", HttpStatus.BAD_REQUEST);
        }
        virtualClockService.simulate(newVirtualTime);
        return new ResponseEntity<>("Simulation done", HttpStatus.OK);
    }

    @GetMapping("/simulateCron")
    @ResponseBody
    public ResponseEntity<?> simulate() {
        virtualClockService.simulateCron();
        System.out.println("Simulation done");
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

}
