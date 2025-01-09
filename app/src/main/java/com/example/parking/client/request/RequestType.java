package com.example.parking.client.request;

public enum RequestType {
    REGISTER(0);

    private final int id;

    RequestType(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * Retrieves the request by its ID.
     * @param id The request ID.
     * @return The requested type.
     */
    public static RequestType getTypeById(int id) {
        for (RequestType t : RequestType.values())
            if (t.getId() == id)
                return t;
        return null;
    }

}
