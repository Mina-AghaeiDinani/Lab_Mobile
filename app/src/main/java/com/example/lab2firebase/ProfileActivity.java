package com.example.lab2firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.Menu;
import android.widget.ImageView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName,tvNameRestaurant,tvMail,tvAddress,tvPhone,tvLocalPhone,tvDescription;
    TextView tvMonday,tvTuesday,tvWednesday,tvThursday,tvFriday,tvSaturday,tvSunday;
    ImageView imgProfile;

    //...
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    //Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.sign_out:
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                    return true;
                case R.id.Orders:

                    return true;
                case R.id.menu:
                    Intent  intent = new Intent(ProfileActivity.this, FoodListActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
    //................Swap between hours and info
    private ConstraintLayout layout;
    private ConstraintSet constraintSetOld = new ConstraintSet();
    private ConstraintSet constraintSetNew = new ConstraintSet();
    private boolean altLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // for swap
        layout = findViewById(R.id.Layout);
        constraintSetOld.clone(layout);
        constraintSetNew.clone(this, R.layout.activity_profile_alternative);
        //navigation

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_profile);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //we have to write codes in different functions
        Toolbar toolbar = findViewById(R.id.toolbarEdit);
        setSupportActionBar(toolbar);
        //end of code related to toolba
        //..authentication
        firebaseAuth=FirebaseAuth.getInstance();
        //reading from data base
        firebaseDatabase=FirebaseDatabase.getInstance();

        //define ids
        tvName=findViewById(R.id.tvName);
        tvNameRestaurant=findViewById(R.id.tvNameRestaurant);
        tvMail=findViewById(R.id.tvMail);
        tvPhone=findViewById(R.id.tvPhone);
        tvLocalPhone=findViewById(R.id.tvLocalPhone);
        tvAddress=findViewById(R.id.tvAddress);
        tvDescription=findViewById(R.id.tvDescription);
        imgProfile=findViewById(R.id.imgPro);

        tvMonday=findViewById(R.id.tvMonday);
        tvTuesday=findViewById(R.id.tvTuesday);
        tvWednesday=findViewById(R.id.tvWednesday);
        tvThursday=findViewById(R.id.tvThursday);
        tvFriday=findViewById(R.id.tvFriday);
        tvSaturday=findViewById(R.id.tvSaturday);
        tvSunday=findViewById(R.id.tvSunday);

        //get reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants");
        databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               ///
                RestaurantsProfile restaurantsProfile=dataSnapshot.getValue(RestaurantsProfile.class);
                tvName.setText(restaurantsProfile.getName());
                tvNameRestaurant.setText(restaurantsProfile.getNamerestaurant());
                tvAddress.setText(restaurantsProfile.getAddress());
                tvPhone.setText(restaurantsProfile.getPhone());
                tvLocalPhone.setText(restaurantsProfile.getPhonerestaurant());
                tvMail.setText(restaurantsProfile.getEmail());
                tvDescription.setText(restaurantsProfile.getDescription());

                Picasso.get()
                        .load(restaurantsProfile.getImageUrl())
                        .placeholder(R.drawable.personal)
                        .fit()
                        .centerCrop()
                        .into(imgProfile);


                tvMonday.setText(restaurantsProfile.getMonday());
                tvTuesday.setText(restaurantsProfile.getTuesday());
                tvWednesday.setText(restaurantsProfile.getWednesday());
                tvThursday.setText(restaurantsProfile.getThursday());
                tvFriday.setText(restaurantsProfile.getFriday());
                tvSaturday.setText(restaurantsProfile.getSaturday());
                tvSunday.setText(restaurantsProfile.getSunday());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        });




    }
    //**************These codes belong to what toolbar is doing
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle toolbar item clicks here.
        int id = item.getItemId();
        //If Edit_button has been pressed go to the Edit activity
        if (id == R.id.btn_edit) {
            Intent i = new Intent(this, EditProfileActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    //End of code related to the toolbar
    //........Swap
    public void swapView(View v){
        TransitionManager.beginDelayedTransition(layout);
        if (!altLayout) {
            constraintSetNew.applyTo(layout);
            altLayout = true;
        } else {
            constraintSetOld.applyTo(layout);
            altLayout = false;
        }
    }
}
