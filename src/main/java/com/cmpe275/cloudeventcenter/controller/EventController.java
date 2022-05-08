package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping()
    public void testAPI(
            @RequestParam String var1
    ) {
        System.out.println("The entered parameter is: " + var1);
    }
}
