package com.alticelabs.mockdata.models;

public class PostRequest {

    private String sagaId;

    private boolean sendResponseEvents;

    public PostRequest() {
    }

    public PostRequest(String sagaId, boolean sendResponseEvents) {
        this.sagaId = sagaId;
        this.sendResponseEvents = sendResponseEvents;
    }

    public String getSagaId() {
        return sagaId;
    }

    public void setSagaId(String sagaId) {
        this.sagaId = sagaId;
    }

    public boolean isSendResponseEvents() {
        return sendResponseEvents;
    }

    public void setSendResponseEvents(boolean sendResponseEvents) {
        this.sendResponseEvents = sendResponseEvents;
    }

}
