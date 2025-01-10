package com.example.parking.client;

import com.example.parking.client.request.RequestHandler;

import java.io.IOException;

import ocsf.client.AbstractClient;
import requests.Message;

public class Client extends AbstractClient {

    private static Client client;
    public static boolean connected = false;
    private boolean awaitResponse = true;
    private Client(String host, int port) {
        super(host, port);
    }

    @Override
    protected void handleMessageFromServer(Object o) {
        awaitResponse = false;
        if (!(o instanceof Message)){
            System.out.println("[Client] - invalid response from Server!");// TODO - better handling
            return;
        }
        Message m = (Message) o;
        RequestHandler.handleRequest(m);
    }

    /**
     * function to send a message to the server
     * */
    public void sendMessageToServer(Message message) {
        new Thread(()-> {
            try
            {
                openConnection();//in order to send a message
                connected = true;
                awaitResponse = true;

                sendToServer(message);
                int loops = 0;
                // wait for response
                while (awaitResponse) {
                    try {
                        Thread.sleep(100); //1000 = 1 second -> 15 seconds = 15000ms
                        loops++;
                        if (loops >= 150){
                            awaitResponse = false;
                            //TODO - handle no response
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch(IOException e)
            {
                connected = false;
                System.out.println("[Client] - Error sending message to server");
                e.printStackTrace();
            }
        }).start();

    }

    /**
     * Initiates the client instance
     */
    public static Client getClient(){
        if (client != null) // if there is already a client instance, return the existing instance
            return client;
        //otherwise create a new instance
        client = new Client(Configs.HOST, Configs.PORT);
        return client;
    }

}
