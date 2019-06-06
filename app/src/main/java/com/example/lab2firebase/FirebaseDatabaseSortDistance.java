package com.example.lab2firebase;

import android.support.annotation.NonNull;
import android.widget.Toast;

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

public class FirebaseDatabaseSortDistance {

    private List<Distance> orderdFoods = new ArrayList<>();
    private String restaurantId1;

    public interface DataStatus{
        void  DataIsLoaded(List<Distance> orderdFoods, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }





    public  void readDistance (final DataStatus dataStatus,final DatabaseReference mReference){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderdFoods.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Distance distance=keyNode.getValue(Distance.class);
                    sortdistance();
                    orderdFoods.add(distance);
                }

                dataStatus.DataIsLoaded(orderdFoods,keys);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void sortdistance() {
        Collections.sort( orderdFoods, new Comparator<Distance>() {
            @Override
            public int compare(Distance o1, Distance o2) {
                return o1.getDistance().compareTo( o2.getDistance() );
            }

        } );
    }

}
