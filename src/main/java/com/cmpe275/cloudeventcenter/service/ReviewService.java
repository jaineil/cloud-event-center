package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.Review;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.repository.ReviewRepository;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    public boolean doesReviewExist(UserInfo reviewer, UserInfo reviewee, Event event) {
        if (reviewRepository.existsByReviewerAndRevieweeAndEvent(reviewer, reviewee, event)) {
            return true;
        } else return false;
    }

    public long addReview(Review review) {
        Review saved = reviewRepository.save(review);
        long id = saved.getReviewId();
        userService.updateUserRating(review.getReviewee().getUserId(), review.getRating(), review.getRevieweeType());
        return id;
    }

    public List<Review> getReviews(UserInfo reviewee, Enum.RevieweeType revieweeType) {
        List <Review> reviews = reviewRepository.findReviewsByRevieweeAndAndRevieweeType(reviewee, revieweeType);
        return reviews;
    }
}
