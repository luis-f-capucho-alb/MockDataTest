package com.alticelabs.mockdata.services;

import com.alticelabs.exagon_communication_lib.consumer.IConsumer;
import com.alticelabs.exagon_communication_lib.exceptions.ExagonCommunicationLibException;
import com.alticelabs.exagon_communication_lib.models.Event;
import com.alticelabs.exagon_communication_lib.producer.IProducer;
import com.alticelabs.exagon_kafka_lib.consumer.KafkaConsumerImpl;
import com.alticelabs.exagon_kafka_lib.producer.KafkaProducerImpl;
import com.alticelabs.mockdata.enums.UServices;
import com.alticelabs.mockdata.factory.ExagonCommandEventFactory;
import com.alticelabs.prototype_common_models.buckets.BucketResult;
import com.alticelabs.prototype_common_models.counters.CounterResult;
import com.alticelabs.prototype_common_models.eligibility.EligibilityResult;
import com.alticelabs.prototype_common_models.ldr.LdrResult;
import com.alticelabs.prototype_common_models.rating.RatingResult;
import com.alticelabs.prototype_common_models.utils.TOPICS;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class CommandEventsService {

    private final IProducer producer;

    private final Map<UServices, Map<String, CompletableFuture<Object>>> completableFutureMap;

    public CommandEventsService() {
        this.producer = new KafkaProducerImpl();
        this.completableFutureMap = new HashMap<>();

        completableFutureMap.put(UServices.ELIGIBILITY, new HashMap<>());
        completableFutureMap.put(UServices.RATING, new HashMap<>());
        completableFutureMap.put(UServices.BUCKETS, new HashMap<>());
        completableFutureMap.put(UServices.COUNTERS, new HashMap<>());
        completableFutureMap.put(UServices.LDR, new HashMap<>());

        Map<String, Class<?>> topicsPerClass = new HashMap<>();
        topicsPerClass.put(TOPICS.ELIGIBILITIES, EligibilityResult.class);
        topicsPerClass.put(TOPICS.RATINGS, RatingResult.class);
        topicsPerClass.put(TOPICS.BUCKETSRESULT, BucketResult.class);
        topicsPerClass.put(TOPICS.COUNTERSRESULT, CounterResult.class);
        topicsPerClass.put(TOPICS.LDRS, LdrResult.class);
        initConsumers(topicsPerClass);
    }

    public void sendEvent(UServices uService, String sagaId) {
        if (uService != UServices.ENTRYSERVICE) {
            CompletableFuture<Object> completableFuture = new CompletableFuture<>();
            completableFutureMap.get(uService).put(sagaId, completableFuture);
        }

        Event event = ExagonCommandEventFactory.getExagonCommand(uService, sagaId);
        try {
            producer.produce(event);
        } catch (ExagonCommunicationLibException e) {
            e.printStackTrace();
        }
    }

    public Object getResponse(UServices uService, String id) {
        try {
            return completableFutureMap.get(uService).get(id).get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    public void initConsumers(Map<String, Class<?>> topicsPerClass) {
        new Thread(() -> {
            IConsumer consumer = new KafkaConsumerImpl();
            try {
                consumer.subscribeEvents(topicsPerClass);
            } catch (ExagonCommunicationLibException e) {
                e.printStackTrace();
            }
            consumer.start();

            while (true) {
                Event event = consumer.getNext();
                if (event == null) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                Object payload = event.getPayload();
                if (payload instanceof EligibilityResult) {
                    addToFuture(UServices.ELIGIBILITY, event.getId(), payload);
                } else if (payload instanceof RatingResult) {
                    addToFuture(UServices.RATING, event.getId(), payload);
                } else if (payload instanceof BucketResult) {
                    addToFuture(UServices.BUCKETS, event.getId(), payload);
                } else if (payload instanceof CounterResult) {
                    addToFuture(UServices.COUNTERS, event.getId(), payload);
                } else if (payload instanceof LdrResult) {
                    addToFuture(UServices.LDR, event.getId(), payload);
                }
            }
        }).start();
    }

    private void addToFuture(UServices uService, String id, Object payload) {
        Map<String, CompletableFuture<Object>> future = completableFutureMap.get(uService);
        if (future.containsKey(id)) {
            future.get(id).complete(payload);
        }
    }

}
