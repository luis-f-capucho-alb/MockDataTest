package com.alticelabs.mockdata.services;

import com.alticelabs.ccp.exagon_common_models.repository.ExagonEntityEvent;
import com.alticelabs.ccp.exagon_communication_lib.exceptions.ExagonCommunicationLibException;
import com.alticelabs.ccp.exagon_communication_lib.producer.IProducer;
import com.alticelabs.ccp.exagon_kafka_lib.producer.KafkaProducerImpl;
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
        ExagonEntityEvent event = ExagonResultEventFactory.getExagonResult(uService, sagaId);
        try {
            producer.produce(event);
        } catch (ExagonCommunicationLibException e) {
            e.printStackTrace();
        }
    }

}
