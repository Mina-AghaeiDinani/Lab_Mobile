package com.example.lab2firebase;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NearestRiderActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_rider);
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
                    //distances.add( new Distance(distance,restId) );
                    //sortdistance();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Reading Distance from database
        //Initiate Recycler view
        mReference = FirebaseDatabase.getInstance().getReference("RiderDistance")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerNearest1);
        new FirebaseDatabaseSortDistance().readDistance(new FirebaseDatabaseSortDistance.DataStatus() {
            @Override
            public void DataIsLoaded(List<Distance> orderdFoods, List<String> keys) {
                new RecyclerView_Distance().setConfig(mRecyclerView, getApplicationContext(), orderdFoods, keys);

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        },mReference);


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
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d("LOCATION", "loc:" + location);
                        if (location != null) {
                            initiallng = location.getLongitude();
                            initiallat = location.getLatitude();
                        }
                    }
                });


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.map_api_key);
        return url;
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
   /* Circle circle = mMap.addCircle(new CircleOptions()
            .center(new LatLng(myPositionMarker.getPosition().latitude,myPositionMarker.getPosition().longitude))
            .radius(10000)
            .strokeColor(Color.parseColor("#2271cce7"))
            .fillColor(Color.parseColor("#2271cce7")));*/

}
