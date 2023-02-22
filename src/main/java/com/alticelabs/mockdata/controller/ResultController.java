package com.alticelabs.mockdata.controller;

import com.alticelabs.mockdata.enums.UServices;
import com.alticelabs.mockdata.services.ResultEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mockdata/result")
public class ResultController {

    @Autowired
    private ResultEventsService resultEventsService;

    @PostMapping("/eligibility/{id}")
    public void sendEligibility(@PathVariable("id") String sagaId) {
        resultEventsService.sendEvent(UServices.ELIGIBILITY, sagaId);
    }

    @PostMapping("/rating/{id}")
    public void sendRating(@PathVariable("id") String sagaId) {
        resultEventsService.sendEvent(UServices.RATING, sagaId);
    }

    @PostMapping("/buckets/{id}")
    public void sendBuckets(@PathVariable("id") String sagaId) {
        resultEventsService.sendEvent(UServices.BUCKETS, sagaId);
    }

    @PostMapping("/counters/{id}")
    public void sendCounter(@PathVariable("id") String sagaId) {
        resultEventsService.sendEvent(UServices.COUNTERS, sagaId);
    }

    @PostMapping("/ldr/{id}")
    public void sendLdr(@PathVariable("id") String sagaId) {
        resultEventsService.sendEvent(UServices.LDR, sagaId);
    }

}
