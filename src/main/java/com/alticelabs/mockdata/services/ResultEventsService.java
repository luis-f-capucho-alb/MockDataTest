package com.alticelabs.mockdata.services;

import com.alticelabs.exagon_communication_lib.exceptions.ExagonCommunicationLibException;
import com.alticelabs.exagon_communication_lib.models.Event;
import com.alticelabs.exagon_communication_lib.producer.IProducer;
import com.alticelabs.exagon_kafka_lib.producer.KafkaProducerImpl;
import com.alticelabs.mockdata.enums.UServices;
import com.alticelabs.mockdata.factory.ExagonResultEventFactory;
import org.springframework.stereotype.Component;

@Component
public class ResultEventsService {

    private final IProducer producer;

    public ResultEventsService() {
        this.producer = new KafkaProducerImpl();
    }

    public void sendEvent(UServices uService, String sagaId) {
        Event event = ExagonResultEventFactory.getExagonResult(uService, sagaId);
        try {
            producer.produce(event);
        } catch (ExagonCommunicationLibException e) {
            e.printStackTrace();
        }
    }

}
