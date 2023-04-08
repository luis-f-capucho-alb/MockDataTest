package com.alticelabs.mockdata.services;

import com.alticelabs.prototype_common_models.buckets.Bucket;
import com.alticelabs.prototype_common_models.buckets.BucketChangedEvent;
import com.alticelabs.prototype_common_models.buckets.BucketType;
import com.alticelabs.prototype_common_models.counters.Counter;
import com.alticelabs.prototype_common_models.counters.CounterEntityEvent;
import com.alticelabs.prototype_common_models.counters.CounterEventType;
import com.alticelabs.prototype_common_models.counters.CountersChange;
import com.alticelabs.prototype_common_models.rules.Rule;
import com.alticelabs.prototype_common_models.rules.events.SaveRuleRequest;
import com.alticelabs.prototype_common_models.tariffs.Tariff;
import com.alticelabs.prototype_common_models.tariffs.TariffEntityEvent;
import com.alticelabs.prototype_common_models.utils.BillingAccount;
import com.alticelabs.prototype_common_models.utils.Operation;
import com.alticelabs.prototype_common_models.utils.TOPICS;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DataService {
    private static final String FILE_PATH_ACCOUNT = "billingAccounts.json";
    private static final String FILE_PATH_COUNTER = "counters.json";
    private static final String FILE_PATH_BUCKET = "buckets.json";
    private final IProducer eventsProducer = new KafkaProducerImpl();

    public List<BillingAccount> loadAllAccountsToKafka() {
        List<BillingAccount> billingAccountFromJson = buildBillingAccountFromJson();
        billingAccountFromJson.forEach(this::saveBillingAccount);
        return billingAccountFromJson;
    }

    public List<Counter> loadAllCountersToKafka() {
        List<Counter> countersListfronJson = buildCountersFromJson();
        countersListfronJson.forEach(this::Vounyrt);
        return countersListfronJson;
    }

    public List<Bucket> loadAllBucketsToKafka() {
        List<Bucket> bucketsListfronJson = buildBucketsFromJson();
        bucketsListfronJson.forEach(this::saveBillingAccount);
        return bucketsListfronJson;
    }

    private List<Counter> buildCountersFromJson() {
        try {
            ObjectMapper mapper = getObjectMapper();
            File jsonFile = new File(FILE_PATH_COUNTER);
            return mapper.readValue(jsonFile, mapper.getTypeFactory().constructCollectionType(List.class, BillingAccount.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<Bucket> buildBucketsFromJson() {
        try {
            ObjectMapper mapper = getObjectMapper();
            File jsonFile = new File(FILE_PATH_BUCKET);
            return mapper.readValue(jsonFile, mapper.getTypeFactory().constructCollectionType(List.class, BillingAccount.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    private List<BillingAccount> buildBillingAccountFromJson() {
        try {
            ObjectMapper mapper = getObjectMapper();
            File jsonFile = new File(FILE_PATH_ACCOUNT);
            return mapper.readValue(jsonFile, mapper.getTypeFactory().constructCollectionType(List.class, BillingAccount.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
    private void saveBillingAccount(BillingAccount billingAccount) {

       // List<Counter> counters = billingAccount.getCounters();
       // List<Bucket> buckets = billingAccount.getBuckets();

        Date eventTimestamp = new Date();
        counters.forEach((counter) -> {
            Chargin counterEvent;
            counterEvent.setEventType(CounterEventType.COUNTER_CREATED);
            counterEvent.setCountersChange(List.of(new CountersChange(counter.getType(),counter.getValue(), Operation.SET)));
            counterEvent.setMsisdn(billingAccount.getMsisdn());
            counterEvent.setTimestamp(eventTimestamp);
            Event event  = new Event(billingAccount.getMsisdn(), TOPICS.COUNTERS, counterEvent);
            try {
                eventsProducer.produce(event);
            } catch (ExagonCommunicationLibException e) {
                e.printStackTrace();
            }
        });

        buckets.forEach((bucket) -> {
            BucketEntityEvent bucketEvent = new BucketEntityEvent();
            bucketEvent.setEventType(BucketEventType.BUCKET_CREATED);
            bucketEvent.setBucketChanges(List.of(new BucketChange(bucket.getType(),bucket.getRemainingValue(), Operation.SET)));
            bucketEvent.setMsisdn(billingAccount.getMsisdn());
            bucketEvent.setTimestamp(eventTimestamp);
            Event event  = new Event(billingAccount.getMsisdn(), TOPICS.BUCKETS, bucketEvent);
            try {
                eventsProducer.produce(event);
            } catch (ExagonCommunicationLibException e) {
                e.printStackTrace();
            }
        });

        tariffs.forEach((tariff) -> {
            TariffEntityEvent tariffEvent = new TariffEntityEvent();
            tariffEvent.setName(tariff.getName());
            tariffEvent.setServiceType(tariff.getServiceType());
            tariffEvent.setTimestamp(eventTimestamp);
            tariffEvent.setMsisdn(billingAccount.getMsisdn());
            Event event = new Event(billingAccount.getMsisdn(),TOPICS.TARIFFS,  tariffEvent);
            try {
                eventsProducer.produce(event);
            } catch (ExagonCommunicationLibException e) {
                e.printStackTrace();
            }
        });
    }

    public SaveRuleRequest loadRuleToKafka(Rule rule) {
        try {
            SaveRuleRequest saveRuleRequest = new SaveRuleRequest(rule.getRuleID(), rule.getRule());
            eventsProducer.produce(new Event(rule.getRuleID(), TOPICS.SAVE_RULE_REQUEST, saveRuleRequest));
            return saveRuleRequest;
        } catch (ExagonCommunicationLibException e) {
            throw new RuntimeException(e);
        }
    }
}
