package com.example.lab2firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class FoodListActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case com.example.lab2firebase.R.id.navigation_home:
                    Intent intent = new Intent(FoodListActivity.this, CircleMenu.class);
                    startActivity(intent);
                    return true;
                case com.example.lab2firebase.R.id.navigation_add:
                    intent = new Intent(FoodListActivity.this, NewDailyOffer.class);
                    startActivity(intent);
                    return true;
                case com.example.lab2firebase.R.id.navigation_sign_out:
                    FirebaseAuth  firebaseAuth=FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(FoodListActivity.this,LoginActivity.class));
                    return true;
            }
            return false;
        }
    };

    private RecyclerView  mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.lab2firebase.R.layout.activity_food_list);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(com.example.lab2firebase.R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mRecyclerView = (RecyclerView) findViewById(com.example.lab2firebase.R.id.recyclerview_food);
        new FirebaseDatabaseHelper().readFoods(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<DailyOffer> dailyOffers, List<String> keys) {
                findViewById(com.example.lab2firebase.R.id.loading_foods_pb).setVisibility(View.GONE);
                new RecyclerView_Config().setConfig(mRecyclerView,FoodListActivity.this,dailyOffers,keys);
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
        });

    }
}
