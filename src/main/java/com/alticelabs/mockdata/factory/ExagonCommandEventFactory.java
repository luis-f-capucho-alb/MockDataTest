package com.alticelabs.mockdata.factory;

import com.alticelabs.ccp.exagon_common_models.repository.ExagonEntityEvent;
import com.alticelabs.mockdata.enums.UServices;


public class ExagonCommandEventFactory {

    public static ExagonEntityEvent getExagonCommand(UServices uService, String sagaId) {
        switch (uService) {
            case ELIGIBILITY:
                return getEligibilityCommandEvent(sagaId);
            case RATING:
                return getRatingCommandEvent(sagaId);
            case BUCKETS:
                return getBucketsCommandEvent(sagaId);
            case COUNTERS:
                return getCountersCommandEvent(sagaId);
            case LDR:
                return getLdrCommandEvent(sagaId);
        }

        return null;
    }

    private static ExagonEntityEvent getEligibilityCommandEvent(String sagaId) {
        Comman commandEligibility = new CommandEligibility(sagaId);
        Event event = new Event(sagaId, TOPICS.CMD_CHECK_ELIGIBILITY, commandEligibility);
        event.addHeader("statusAddress", "status");
        return event;
    }

    private static ExagonEntityEvent getRatingCommandEvent(String sagaId) {
        CommandRating commandRating = new CommandRating(sagaId);
        Event event = new Event(sagaId, TOPICS.CMD_CHECK_RATING, commandRating);
        event.addHeader("statusAddress", "status");
        return event;
    }

    private static ExagonEntityEvent getBucketsCommandEvent(String sagaId) {
        CommandBuckets commandBuckets = new CommandBuckets(sagaId);
        Event event = new Event(sagaId, TOPICS.CMD_CHARGING_BUCKET, commandBuckets);
        event.addHeader("statusAddress", "status");
        return event;
    }

    private static ExagonEntityEvent getCountersCommandEvent(String sagaId) {
        CommandCounters commandCounters = new CommandCounters(sagaId);
        Event event = new Event(sagaId, TOPICS.CMD_UPDATE_COUNTERS, commandCounters);
        event.addHeader("statusAddress", "status");
        return event;
    }

    private static ExagonEntityEvent getLdrCommandEvent(String sagaId) {
        CommandLdr commandLdr = new CommandLdr(sagaId);
        Event event = new Event(sagaId, TOPICS.CMD_CREATE_LDR, commandLdr);
        event.addHeader("statusAddress", "status");
        return event;
    }

}
