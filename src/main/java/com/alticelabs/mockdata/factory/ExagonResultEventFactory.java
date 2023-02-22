package com.alticelabs.mockdata.factory;

import com.alticelabs.exagon_communication_lib.models.Event;
import com.alticelabs.mockdata.enums.UServices;
import com.alticelabs.prototype_common_models.buckets.*;
import com.alticelabs.prototype_common_models.counters.CounterEventType;
import com.alticelabs.prototype_common_models.counters.CounterResult;
import com.alticelabs.prototype_common_models.counters.CounterType;
import com.alticelabs.prototype_common_models.counters.CountersChange;
import com.alticelabs.prototype_common_models.eligibility.EligibilityResult;
import com.alticelabs.prototype_common_models.eligibility.EligibilityStatus;
import com.alticelabs.prototype_common_models.ldr.LdrResult;
import com.alticelabs.prototype_common_models.orchestrator.CoreRequest;
import com.alticelabs.prototype_common_models.orchestrator.Request;
import com.alticelabs.prototype_common_models.orchestrator.ServiceType;
import com.alticelabs.prototype_common_models.rating.RatingResult;
import com.alticelabs.prototype_common_models.tariffs.TariffName;
import com.alticelabs.prototype_common_models.utils.Operation;
import com.alticelabs.prototype_common_models.utils.TOPICS;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ExagonResultEventFactory {

    public static Event getExagonResult(UServices uService, String sagaId) {
        switch (uService) {
            case ENTRYSERVICE:
                return getEntryServiceResultEvent(sagaId);
            case ELIGIBILITY:
                return getEligibilityResultEvent(sagaId);
            case RATING:
                return getRatingResultEvent(sagaId);
            case BUCKETS:
                return getBucketsResultEvent(sagaId);
            case COUNTERS:
                return getCountersResultEvent(sagaId);
            case LDR:
                return getLdrResultEvent(sagaId);
        }

        return null;
    }

    private static Event getEntryServiceResultEvent(String sagaId) {
        CoreRequest coreRequest = new CoreRequest(sagaId, "MARIA_123", new Timestamp(new Date().getTime()), false, 10d, ServiceType.A);
        Request request = new Request();
        request.buildRequest(coreRequest);
        request.setRuleID("1");

        return new Event(sagaId, TOPICS.REQUESTS, request);
    }

    private static Event getEligibilityResultEvent(String sagaId) {
        EligibilityResult eligibilityResult = new EligibilityResult(sagaId, EligibilityStatus.ACCEPTED, TariffName.Alpha1, "eligible");
        return new Event(sagaId, TOPICS.ELIGIBILITIES, eligibilityResult);
    }

    private static Event getRatingResultEvent(String sagaId) {
        RatingResult ratingResult = new RatingResult(sagaId, 5d, TariffName.Alpha1);
        return new Event(sagaId, TOPICS.RATINGS, ratingResult);
    }

    private static Event getBucketsResultEvent(String sagaId) {
        List<BucketChange> bucketChanges = new LinkedList<>();
        bucketChanges.add(new BucketChange(BucketType.A, 1d, Operation.CREDIT));

        BucketResult bucketResult = new BucketResult(sagaId, BucketEventType.BUCKET_CHANGED, null, bucketChanges, BucketStatus.FULLY_CHARGED);
        return new Event(sagaId, TOPICS.BUCKETS, bucketResult);
    }

    private static Event getCountersResultEvent(String sagaId) {
        List<CountersChange> countersChanges = new LinkedList<>();
        countersChanges.add(new CountersChange(CounterType.A, 1d, Operation.CREDIT));

        CounterResult counterResult = new CounterResult(sagaId, CounterEventType.COUNTER_CHANGED, countersChanges);
        return new Event(sagaId, TOPICS.COUNTERS, counterResult);
    }

    private static Event getLdrResultEvent(String sagaId) {
        LdrResult ldrResult = new LdrResult(sagaId,
                (EligibilityResult) getEligibilityResultEvent(sagaId).getPayload(),
                (RatingResult) getRatingResultEvent(sagaId).getPayload(),
                (BucketResult) getBucketsResultEvent(sagaId).getPayload(),
                (CounterResult) getCountersResultEvent(sagaId).getPayload());
        return new Event(sagaId, TOPICS.LDRS, ldrResult);
    }

}
