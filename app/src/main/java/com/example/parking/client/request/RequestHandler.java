package com.example.parking.client.request;

import requests.Message;

public class RequestHandler {

    public static void handleRequest(Message msg){

        switch (msg.getRequestEnumType()){
            case REGISTER:
                if (msg.isSuccess())
                    System.out.println("Successfully Registered!");
                else
                    System.out.println("Something went wrong registering...!");
                return;
            case INVALID_DATATYPE:
                return;
            default:
                System.out.println("[RequestHandler] - unhandled request type");
        }

    }

}
