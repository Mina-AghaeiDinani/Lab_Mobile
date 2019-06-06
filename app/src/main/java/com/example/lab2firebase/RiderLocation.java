package com.example.lab2firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiderLocation {
    private String key;
    private LatLng latLng;

    public RiderLocation(String key, LatLng latLng){
        this.key = key;
        this.latLng = latLng;
    }

    public void setKey(String key){
        this.key = key;
    }


    public void setLatLng(LatLng latLng){
        this.latLng = latLng;
    }

    public LatLng getLatLng() {
        return latLng;
    }


    public String getKey() {
        return key;
    }
}
