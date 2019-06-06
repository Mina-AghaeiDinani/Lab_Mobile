package com.example.lab2firebase;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NearestRiderService extends IntentService {

    private DatabaseReference mReferenceLocations, mReferenceLocations2;
    private DatabaseReference mReference;
    private ArrayList<RiderLocation> riderLocations;
    TextView textView;
    String mystring;
    List<LocationReader> locationReaders = new ArrayList<>();
    List<String> keys = new ArrayList<>();
    List<Distance> distances = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private FirebaseAuth firebaseAuth;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    private ArrayList<MarkerOptions> places;
    MarkerOptions place1, place2;
    Marker myPositionMarker;
    private Button getdirection;
    double initiallng, initiallat;
    DatabaseReference databaseOrder;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *  Used to name the worker thread, important only for debugging.
     */
    public NearestRiderService() {
        super("intent_name");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final String orderID = intent.getStringExtra("order_id");
        riderLocations = new ArrayList<>();
        places = new ArrayList<>();
        mReferenceLocations = FirebaseDatabase.getInstance().getReference().child("RidersLocation");

        //We get the location of the Restaurant
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        mReferenceLocations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                riderLocations.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    LocationReader locationReader = keyNode.getValue(LocationReader.class);
                    locationReaders.add(locationReader);

                    // We store the value read inside an array of RestaurantLocation
                    LatLng thisLatLng = new LatLng(locationReader.getLat(), locationReader.getLng());
                    RiderLocation thisRideLocation = new RiderLocation(keyNode.getKey(), thisLatLng);
                    riderLocations.add(thisRideLocation);
                }
                int i;
                double distance;

                for (i = 0; i < riderLocations.size(); i++) {
                    distance = CalculationByDistance(initiallat,initiallng, riderLocations.get(i).getLatLng().latitude, riderLocations.get(i).getLatLng().longitude);
                    String riderId = (riderLocations.get(i).getKey());
                    //................
                    //....
                    Distance distance1 = new Distance();
                    distance1.setDistance(distance);
                    distance1.setRiderId(riderId);

                    FirebaseDatabase.getInstance().getReference("RiderDistance")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(riderId).setValue(distance1);
                    //..............
                    distances.add( new Distance(distance,riderId) );
                    sortdistance();
                    databaseOrder = FirebaseDatabase.getInstance().getReference()
                            .child("OrderInfo").child(orderID);
                    databaseOrder.child("riderId").setValue(distances.get(0).getRiderId());

                }
                Log.d("DIST SORTED", ""+distances);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.d("LOCATION", "loc:" + location);
                if (location != null) {
                    initiallng = location.getLongitude();
                    initiallat = location.getLatitude();
                }
            }
        });



        //TODO add code ot find the nearest rider activity
    }

    public double CalculationByDistance(double initialLat, double initialLong, double finalLat, double finalLong) {

        double latDiff = finalLat - initialLat;
        double longDiff = finalLong - initialLong;
        double earthRadius = 6371; //In Km if you want the distance in km

        double distance = 2 * earthRadius * Math.asin(Math.sqrt(Math.pow(Math.sin(latDiff / 2.0), 2) + Math.cos(initialLat) * Math.cos(finalLat) * Math.pow(Math.sin(longDiff / 2), 2)));


        return distance;
    }

    public void sortdistance() {
        Collections.sort(distances, new Comparator<Distance>() {
            @Override
            public int compare(Distance o1, Distance o2) {
                return o1.getDistance().compareTo(o2.getDistance());
            }

        });
    }
}
