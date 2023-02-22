package com.alticelabs.mockdata.controller;

import com.alticelabs.mockdata.enums.UServices;
import com.alticelabs.mockdata.services.CommandEventsService;
import com.alticelabs.prototype_common_models.buckets.BucketResult;
import com.alticelabs.prototype_common_models.counters.CounterResult;
import com.alticelabs.prototype_common_models.eligibility.EligibilityResult;
import com.alticelabs.prototype_common_models.ldr.LdrResult;
import com.alticelabs.prototype_common_models.rating.RatingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mockdata/command")
public class CommandController {

    @Autowired
    private CommandEventsService commandEventsService;

    @PostMapping("/entryservice/{id}")
    public void sendRequest(@PathVariable("id") String sagaId) {
        commandEventsService.sendEvent(UServices.ENTRYSERVICE, sagaId);
    }

    @PostMapping("/eligibility/{id}")
    public EligibilityResult sendEligibility(@PathVariable("id") String sagaId) {
        commandEventsService.sendEvent(UServices.ELIGIBILITY, sagaId);
        return (EligibilityResult) commandEventsService.getResponse(UServices.ELIGIBILITY, sagaId);
    }

    @PostMapping("/rating/{id}")
    public RatingResult sendRating(@PathVariable("id") String sagaId) {
        commandEventsService.sendEvent(UServices.RATING, sagaId);
        return (RatingResult) commandEventsService.getResponse(UServices.RATING, sagaId);
    }

    @PostMapping("/buckets/{id}")
    public BucketResult sendBuckets(@PathVariable("id") String sagaId) {
        commandEventsService.sendEvent(UServices.BUCKETS, sagaId);
        return (BucketResult) commandEventsService.getResponse(UServices.BUCKETS, sagaId);
    }

    @PostMapping("/counters/{id}")
    public CounterResult sendCounter(@PathVariable("id") String sagaId) {
        commandEventsService.sendEvent(UServices.COUNTERS, sagaId);
        return (CounterResult) commandEventsService.getResponse(UServices.COUNTERS, sagaId);
    }

    @PostMapping("/ldr/{id}")
    public LdrResult sendLdr(@PathVariable("id") String sagaId) {
        commandEventsService.sendEvent(UServices.LDR, sagaId);
        return (LdrResult) commandEventsService.getResponse(UServices.LDR, sagaId);
    }

}
