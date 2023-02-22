package com.alticelabs.mockdata.services;

import com.alticelabs.exagon_communication_lib.exceptions.ExagonCommunicationLibException;
import com.alticelabs.exagon_communication_lib.models.Event;
import com.alticelabs.exagon_communication_lib.producer.IProducer;
import com.alticelabs.exagon_kafka_lib.producer.KafkaProducerImpl;
import com.alticelabs.prototype_common_models.buckets.Bucket;
import com.alticelabs.prototype_common_models.buckets.BucketChange;
import com.alticelabs.prototype_common_models.buckets.BucketEntityEvent;
import com.alticelabs.prototype_common_models.buckets.BucketEventType;
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
    private static final String FILE_PATH = "billingAccounts.json";
    private final IProducer eventsProducer = new KafkaProducerImpl();

    public List<BillingAccount> loadAllAccountsToKafka() {
        List<BillingAccount> billingAccountFromJson = buildBillingAccountFromJson();
        billingAccountFromJson.forEach(this::saveBillingAccount);
        return billingAccountFromJson;
    }

    private List<BillingAccount> buildBillingAccountFromJson() {
        try {
            ObjectMapper mapper = getObjectMapper();
            File jsonFile = new File(FILE_PATH);
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
        List<Counter> counters = billingAccount.getCounters();
        List<Bucket> buckets = billingAccount.getBuckets();
        List<Tariff> tariffs = billingAccount.getTariffs();
        Date eventTimestamp = new Date();
        counters.forEach((counter) -> {
            CounterEntityEvent counterEvent = new CounterEntityEvent();
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
