package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.service.SystemReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/systemReport")
public class SystemReportController {

    @Autowired
    private SystemReportService systemReportService;

    @GetMapping("/get/{currentTime}")
    public ResponseEntity<?> getSystemReportForLast90Days (
        @PathVariable("currentTime") String currentTime
    ) {
        LocalDateTime current = LocalDateTime.parse(currentTime);
        Map<String, Object> report = systemReportService.getNumberOfEventsAndPaidEvents(current);
        Map<String, Object> cancelledEventsReport = systemReportService.getNumberOfCanceledEvents(current);
        Map<String, Object> finishedEventsReport = systemReportService.getNumberOfFinishedEvents(current);
        report.putAll(cancelledEventsReport);
        report.putAll(finishedEventsReport);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
