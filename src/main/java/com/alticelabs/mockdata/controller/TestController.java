package com.alticelabs.mockdata.controller;

import com.alticelabs.common.models.CommandCreateAccount;
import com.alticelabs.mockdata.models.PostRequest;
import com.alticelabs.mockdata.services.TestService;
import com.alticelabs.prototype_common_models.buckets.BucketResult;
import com.alticelabs.prototype_common_models.counters.CounterResult;
import com.alticelabs.prototype_common_models.eligibility.EligibilityResult;
import com.alticelabs.prototype_common_models.ldr.LdrResult;
import com.alticelabs.prototype_common_models.rating.RatingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mockdata/test")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/eligibility")
    public EligibilityResult testEligbility(@RequestBody PostRequest postRequest) {
        String sagaId = postRequest.getSagaId();
        boolean sendResponseEvents = postRequest.isSendResponseEvents();
        return testService.testEligbility(sagaId, sendResponseEvents);
    }

    @PostMapping("/rating")
    public RatingResult sendRating(@RequestBody PostRequest postRequest) {
        String sagaId = postRequest.getSagaId();
        boolean sendResponseEvents = postRequest.isSendResponseEvents();
        return testService.testRating(sagaId, sendResponseEvents);
    }

    @PostMapping("/buckets")
    public BucketResult sendBuckets(@RequestBody PostRequest postRequest) {
        String sagaId = postRequest.getSagaId();
        boolean sendResponseEvents = postRequest.isSendResponseEvents();
        return testService.testBuckets(sagaId, sendResponseEvents);
    }

    @PostMapping("/counters")
    public CounterResult sendCounter(@RequestBody PostRequest postRequest) {
        String sagaId = postRequest.getSagaId();
        boolean sendResponseEvents = postRequest.isSendResponseEvents();
        return testService.testCounter(sagaId, sendResponseEvents);
    }

    @PostMapping("/ldr")
    public LdrResult sendLdr(@RequestBody PostRequest postRequest) {
        String sagaId = postRequest.getSagaId();
        boolean sendResponseEvents = postRequest.isSendResponseEvents();
        return testService.testLdr(sagaId, sendResponseEvents);
    }

}
