package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.Review;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.service.*;
import com.cmpe275.cloudeventcenter.utils.EmailNotifierService;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRegistrationService eventRegistrationService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailNotifierService emailNotifierService;

    @PostMapping("/add")
    public ResponseEntity<?> addReview (
            @RequestBody Map<?,?> reviewReq
    ) {

        String revieweeId = String.valueOf(reviewReq.get("revieweeId"));
        UserInfo reviewee = userService.getUserInfo(revieweeId);

        String reviewerId = String.valueOf(reviewReq.get("reviewerId"));
        UserInfo reviewer = userService.getUserInfo(reviewerId);

        String eventIdStr = String.valueOf(reviewReq.get("eventId"));
        long eventId = Integer.parseInt(eventIdStr);
        Event event = eventService.getEventById(eventId);

        // checks if the same reviewer has reviewed the same reviewee for same event before
        if (reviewService.doesReviewExist(reviewer, reviewee, event)) {
            return new ResponseEntity<>("Already reviewed", HttpStatus.BAD_REQUEST);
        }

        // checks review time window
        LocalDateTime eventStart = event.getStartTime();
        LocalDateTime eventEnd = event.getEndTime();
        String currentTimeStr = String.valueOf(reviewReq.get("currentTime"));
        LocalDateTime incomingTime = LocalDateTime.parse(currentTimeStr);

        if (incomingTime.isBefore(eventStart)) {
            return new ResponseEntity<>("Can not review before event starts", HttpStatus.BAD_REQUEST);
        }

        if (incomingTime.isAfter(eventEnd.plusDays(7))) {
            return new ResponseEntity<>("Reviewing window closed", HttpStatus.BAD_REQUEST);
        }

        String revieweeTypeStr = String.valueOf(reviewReq.get("revieweeType"));
        Enum.RevieweeType revieweeType = Enum.RevieweeType.valueOf(revieweeTypeStr);

        // check if a registered and approved user is posting (REVIEWER) the review or not
        if (revieweeType.equals(Enum.RevieweeType.Organizer)) {
            if (!eventRegistrationService.isUserRegistered(event, reviewer)) {
                return new ResponseEntity<>("Can not review an event without registering", HttpStatus.BAD_REQUEST);
            }
        }

        // checks if organizer is reviewing a registered and approved user (REVIEWEE) or not
        if (revieweeType.equals(Enum.RevieweeType.Participant)) {
            if (!eventRegistrationService.isUserRegistered(event, reviewee)) {
                return new ResponseEntity<>("Can not review a non-approved participant", HttpStatus.BAD_REQUEST);
            }
        }

        String ratingStr = String.valueOf(reviewReq.get("rating"));
        int rating = Integer.parseInt(ratingStr);
        String textFeedback = String.valueOf(reviewReq.get("textFeedback"));

        Review review = Review.builder()
                .reviewee(UserInfo.builder().userId(revieweeId).build())
                .reviewer(UserInfo.builder().userId(reviewerId).build())
                .event(Event.builder().eventId(eventId).build())
                .rating(rating)
                .revieweeType(revieweeType)
                .textFeedback(textFeedback)
                .build();

        long reviewId = reviewService.addReview(review);

        return new ResponseEntity<>("Inserted review with id: " + reviewId, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getReviewsOnUser(
        @RequestParam String revieweeId,
        @RequestParam String revieweeType
    ) {

        UserInfo reviewee = userService.getUserInfo(revieweeId);
        Enum.RevieweeType revieweeTypeParsed = Enum.RevieweeType.valueOf(revieweeType);

        List<Review> reviews = reviewService.getReviews(reviewee, revieweeTypeParsed);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    public void onAddReview(Review review){
        String revieweeId = review.getReviewee().getUserId();
        String reviewerId = review.getReviewer().getUserId();
        UserInfo reviewee = userService.getUserInfo(revieweeId);
        UserInfo reviewer = userService.getUserInfo(reviewerId);
        String to= reviewee.getEmailId();
//        String eventId = review.getEvent();
        long eventId = review.getEvent().getEventId();

        String eventTitle= eventService.getEventById(eventId).getTitle();
        String reviewText = review.getTextFeedback();
        String reviewerScreenName = reviewer.getScreenName();

        System.out.println(revieweeId);

        System.out.println(eventTitle);
        System.out.println(reviewText);
        System.out.println(reviewerScreenName);

        emailNotifierService.notify(
                to,
                "CEC Review Notification",
                "Hi, \n\n You received a review for event "+ eventTitle +
                        " from "+ reviewerScreenName+
                        "\n  `"+ reviewText +"`"+
                        "\n \n CEC Team"
        );

    }
}
