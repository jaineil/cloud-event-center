package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.Review;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findReviewsByRevieweeAndAndRevieweeType(UserInfo reviewee, Enum.RevieweeType revieweeType);
    boolean existsByReviewerAndRevieweeAndEvent(UserInfo reviewer, UserInfo reviewee, Event event);
}
