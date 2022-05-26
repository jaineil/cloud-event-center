package com.cmpe275.cloudeventcenter.aspect;

import com.cmpe275.cloudeventcenter.controller.*;
import com.cmpe275.cloudeventcenter.model.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EmailAspect {

    @Autowired
    EventController eventController;

    @Autowired
    EventRegistrationController eventRegistrationController;

    @Autowired
    ParticipantForumController participantForumController;

    @Autowired
    SignupForumController signupForumController;

    @Autowired
    ReviewController reviewController;



    @After("execution(public * com.cmpe275.cloudeventcenter.service.EventService.insert(..)) && args(event)")
    public void postEventCreation(JoinPoint joinPoint, Event event ) {
        eventController.onEventCreatedMail(event);
    }

    @After("execution(public * com.cmpe275.cloudeventcenter.service.EventRegistrationService.insert(..)) && args(eventRegistration)")
    public void postEventSignup(JoinPoint joinPoint, EventRegistration eventRegistration ) {
        eventRegistrationController.onEventSignupMail(eventRegistration);
    }

    @After("execution(public * com.cmpe275.cloudeventcenter.service.EventRegistrationService.approveRegistration(..)) && args(registrationId)")
    public void postEventRegistrationApproval(JoinPoint joinPoint, Long registrationId ) {
        eventRegistrationController.onEventRegistrationApproval(registrationId);
    }

    //CREATE EVENT SIGNUP DECLINE API
    @After("execution(public * com.cmpe275.cloudeventcenter.service.EventRegistrationService.declineRegistration(..)) && args(registrationId)")
    public void postEventRegistrationDecline(JoinPoint joinPoint, Long registrationId ) {
        eventRegistrationController.onEventRegistrationDecline(registrationId);
    }
    @After("execution(public * com.cmpe275.cloudeventcenter.service.SignupForumService.addMessageToSignupForum(..)) && args(message)")
    public void postNewMessageInSignupForum(JoinPoint joinPoint, SignupForum message ) {
        System.out.println("new message posted in signup forum");
        signupForumController.onNewMessageInSignUpForum(message);
    }

    @After("execution(public * com.cmpe275.cloudeventcenter.service.ParticipantForumService.addMessageToSignupForum(..)) && args(message)")
    public void postNewMessageInParticipantForum(JoinPoint joinPoint, ParticipantForum message ) {
        participantForumController.onNewMessageInParticipantForum(message);
    }

    @After("execution(public * com.cmpe275.cloudeventcenter.service.ReviewService.addReview(..)) && args(review)")
    public void postAddReview(JoinPoint joinPoint, Review review ) {
        reviewController.onAddReview(review);
    }
}
