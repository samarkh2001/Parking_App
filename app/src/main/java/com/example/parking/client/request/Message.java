package com.example.parking.client.request;

import java.io.Serializable;

public class Message implements Serializable {

    private Object requestData;
    private int requestType;

    /**
     * Constructs a new Message with the specified request type and request data.
     *
     * @param requestType The type of the request.
     * @param requestData The data associated with the request.
     */
    public Message(RequestType requestType, Object requestData) {
        this.requestType = requestType.getId();
        this.requestData = requestData;
    }
}
