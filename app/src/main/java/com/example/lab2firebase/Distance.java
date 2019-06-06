package com.example.lab2firebase;

public class Distance {
    //Property name must be the same as what we defined in real time database
    private String riderId;

    private Double distance;

    public Distance() {
        //Constructor , it is needed
    }

    public Distance(Double distance, String riderId
    ) {


        this.distance=distance;
        this.riderId = riderId;

    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String customerId) {
        this.riderId = customerId;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}