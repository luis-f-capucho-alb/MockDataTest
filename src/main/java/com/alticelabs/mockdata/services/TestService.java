package com.alticelabs.mockdata.services;

import com.alticelabs.mockdata.enums.UServices;
import com.alticelabs.prototype_common_models.buckets.BucketResult;
import com.alticelabs.prototype_common_models.counters.CounterResult;
import com.alticelabs.prototype_common_models.eligibility.EligibilityResult;
import com.alticelabs.prototype_common_models.ldr.LdrResult;
import com.alticelabs.prototype_common_models.rating.RatingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private ResultEventsService resultEventsService;

    @Autowired
    private CommandEventsService commandEventsService;

    public EligibilityResult testEligbility(String sagaId, boolean sendResponseEvents) {
        if (sendResponseEvents) {
            resultEventsService.sendEvent(UServices.ENTRYSERVICE, sagaId);
        }

        commandEventsService.sendEvent(UServices.ELIGIBILITY, sagaId);
        return (EligibilityResult) commandEventsService.getResponse(UServices.ELIGIBILITY, sagaId);
    }

    public RatingResult testRating(String sagaId, boolean sendResponseEvents) {
        if (sendResponseEvents) {
            resultEventsService.sendEvent(UServices.ENTRYSERVICE, sagaId);
            resultEventsService.sendEvent(UServices.ELIGIBILITY, sagaId);
        }

        commandEventsService.sendEvent(UServices.RATING, sagaId);
        return (RatingResult) commandEventsService.getResponse(UServices.RATING, sagaId);
    }

    public BucketResult testBuckets(String sagaId, boolean sendResponseEvents) {
        if (sendResponseEvents) {
            resultEventsService.sendEvent(UServices.ENTRYSERVICE, sagaId);
            resultEventsService.sendEvent(UServices.ELIGIBILITY, sagaId);
            resultEventsService.sendEvent(UServices.RATING, sagaId);
        }

        commandEventsService.sendEvent(UServices.BUCKETS, sagaId);
        return (BucketResult) commandEventsService.getResponse(UServices.BUCKETS, sagaId);
    }

    public CounterResult testCounter(String sagaId, boolean sendResponseEvents) {
        if (sendResponseEvents) {
            resultEventsService.sendEvent(UServices.ENTRYSERVICE, sagaId);
            resultEventsService.sendEvent(UServices.ELIGIBILITY, sagaId);
            resultEventsService.sendEvent(UServices.RATING, sagaId);
            resultEventsService.sendEvent(UServices.BUCKETS, sagaId);
        }

        commandEventsService.sendEvent(UServices.COUNTERS, sagaId);
        return (CounterResult) commandEventsService.getResponse(UServices.COUNTERS, sagaId);
    }

    public LdrResult testLdr(String sagaId, boolean sendResponseEvents) {
        if (sendResponseEvents) {
            resultEventsService.sendEvent(UServices.ENTRYSERVICE, sagaId);
            resultEventsService.sendEvent(UServices.ELIGIBILITY, sagaId);
            resultEventsService.sendEvent(UServices.RATING, sagaId);
            resultEventsService.sendEvent(UServices.BUCKETS, sagaId);
            resultEventsService.sendEvent(UServices.COUNTERS, sagaId);
        }

        commandEventsService.sendEvent(UServices.LDR, sagaId);
        return (LdrResult) commandEventsService.getResponse(UServices.LDR, sagaId);
    }

}
