package com.alticelabs.mockdata.factory;

import com.alticelabs.exagon_communication_lib.models.Event;
import com.alticelabs.mockdata.enums.UServices;
import com.alticelabs.prototype_common_models.buckets.CommandBuckets;
import com.alticelabs.prototype_common_models.counters.CommandCounters;
import com.alticelabs.prototype_common_models.eligibility.CommandEligibility;
import com.alticelabs.prototype_common_models.ldr.CommandLdr;
import com.alticelabs.prototype_common_models.rating.CommandRating;
import com.alticelabs.prototype_common_models.utils.TOPICS;

public class ExagonCommandEventFactory {

    public static Event getExagonCommand(UServices uService, String sagaId) {
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

    private static Event getEligibilityCommandEvent(String sagaId) {
        CommandEligibility commandEligibility = new CommandEligibility(sagaId);
        Event event = new Event(sagaId, TOPICS.CMD_CHECK_ELIGIBILITY, commandEligibility);
        event.addHeader("statusAddress", "status");
        return event;
    }

    private static Event getRatingCommandEvent(String sagaId) {
        CommandRating commandRating = new CommandRating(sagaId);
        Event event = new Event(sagaId, TOPICS.CMD_CHECK_RATING, commandRating);
        event.addHeader("statusAddress", "status");
        return event;
    }

    private static Event getBucketsCommandEvent(String sagaId) {
        CommandBuckets commandBuckets = new CommandBuckets(sagaId);
        Event event = new Event(sagaId, TOPICS.CMD_CHARGING_BUCKET, commandBuckets);
        event.addHeader("statusAddress", "status");
        return event;
    }

    private static Event getCountersCommandEvent(String sagaId) {
        CommandCounters commandCounters = new CommandCounters(sagaId);
        Event event = new Event(sagaId, TOPICS.CMD_UPDATE_COUNTERS, commandCounters);
        event.addHeader("statusAddress", "status");
        return event;
    }

    private static Event getLdrCommandEvent(String sagaId) {
        CommandLdr commandLdr = new CommandLdr(sagaId);
        Event event = new Event(sagaId, TOPICS.CMD_CREATE_LDR, commandLdr);
        event.addHeader("statusAddress", "status");
        return event;
    }

}
