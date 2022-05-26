package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.service.UserLevelReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@CrossOrigin(origins = "http://18.144.15.109:3000")
@RestController
@RequestMapping("/userReport")
public class UserLevelReportController {
    @Autowired
    private UserLevelReportService userLevelReportService;

    @GetMapping("/get/{userId}/{currentTime}")
    public ResponseEntity<?> getUserLevelReportForLast90Days (
            @PathVariable("userId") String userId,
            @PathVariable("currentTime") String currentTime
    ) {
        LocalDateTime current = LocalDateTime.parse(currentTime);
        Map<String, Object> report = userLevelReportService.getNumberOfEventsForParticipant(current, userId);
        Map<String, Object> rejected = userLevelReportService.getNumberOfRejectsForParticipant(current, userId);
        Map<String, Object> accepted = userLevelReportService.getNumberOfAcceptsForParticipant(current, userId);
        Map<String, Object> finished = userLevelReportService.getNumberOfFinishedEventsForParticipant(current, userId);
        Map<String, Object> orgReport = userLevelReportService.getNumberOfEventsCreatedForOrganizer(current, userId);
        Map<String, Object> orgCancelled = userLevelReportService.getNumberOfCancelledEventsForOrganizer(current, userId);
        Map<String, Object> orgRatio = userLevelReportService.getRatioOfParticipantRequestsToMinimumRequiredParticipants(current, userId);
        Map<String, Object> orgAvg = userLevelReportService.getAverageNumberOfParticipantsAmongFinishedEventsForOrganization(current, userId);
        Map<String, Object> orgRevenue = userLevelReportService.getRevenueOfPaidAndFinishedEventsForOrganizer(current, userId);
        report.putAll(rejected);
        report.putAll(accepted);
        report.putAll(finished);
        report.putAll(orgReport);
        report.putAll(orgCancelled);
        report.putAll(orgRatio);
        report.putAll(orgAvg);
        report.putAll(orgRevenue);

        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
