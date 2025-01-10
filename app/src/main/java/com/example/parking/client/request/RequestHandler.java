package com.example.parking.client.request;

import com.example.parking.client.Client;

import commons.entities.User;
import commons.requests.Message;

public class RequestHandler {

    public static void handleRequest(Message msg){

        switch (msg.getRequestEnumType()){
            case REGISTER:
                if (msg.isSuccess())
                    System.out.println("Successfully Registered!");
                else
                    System.out.println("Something went wrong registering...!");
                return;
            case LOGIN:
                if (msg.isSuccess() && msg.getResponse() instanceof User){
                    Client.debug("RequestHandler@LOGIN", "User logging in");
                    Client.loggedInUser = (User) msg.getResponse();
                }else{
                    Client.debug("RequestHandler@LOGIN", "invalid login request");
                    Client.loggedInUser = null;
                }
                return;
            case INVALID_DATATYPE:
                return;
            default:
                System.out.println("[RequestHandler] - unhandled request type");
        }

    }

}
