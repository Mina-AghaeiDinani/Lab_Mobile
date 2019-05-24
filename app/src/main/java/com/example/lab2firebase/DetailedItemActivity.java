package com.example.lab2firebase;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailedItemActivity extends AppCompatActivity implements Serializable{
    ListView itemsListView;
    TextView orderNumber;
    TextView totalPrice;
    private DatabaseReference databaseFoodOrdered;
    private String myRestaurantID;
    ArrayList<OrderdFood> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.lab2firebase.R.layout.activity_detailed_item);

        itemsListView = (ListView) findViewById(com.example.lab2firebase.R.id.itemsListView);
        orderNumber = (TextView) findViewById(com.example.lab2firebase.R.id.orderNb_textView);
        totalPrice = (TextView) findViewById(com.example.lab2firebase.R.id.totalPrice_textView);

        myRestaurantID =  FirebaseAuth.getInstance().getCurrentUser().getUid();

        foodList = new ArrayList<>();

        Intent myIntent = getIntent();
        final CartInfo current_cart = (CartInfo) myIntent.getSerializableExtra("Order_clicked");
        Log.d("ONDATACHANGE2", "ordered id:"+current_cart.getOrderedId());

        //We retrieve the Food belonging to this order
        databaseFoodOrdered = FirebaseDatabase.getInstance().getReference("OrderFoods");

        databaseFoodOrdered.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodList.clear();
                for (DataSnapshot orderSnap : dataSnapshot.getChildren()){
                    Log.d("ONDATACHANGE3", "ordered id:"+current_cart.getOrderedId());
                    Log.d("ONDATACHANGE4", "ordered id:"+orderSnap.getKey());

                    if ((orderSnap.getKey()).equals(current_cart.getOrderedId())) {

                        Log.d("ENTERING IF", "ordered id:"+orderSnap.getKey());

                        for (DataSnapshot foodSnap : orderSnap.getChildren()) {
                            OrderdFood thisFood = (OrderdFood) foodSnap.getValue(OrderdFood.class);
                            foodList.add(thisFood);
                        }

                        OrderdFoodAdapter orderdFoodAdapter = new OrderdFoodAdapter(getApplicationContext(), foodList);
                        itemsListView.setAdapter(orderdFoodAdapter);

                        orderNumber.setText("Order " + current_cart.getOrderNum());

                        totalPrice.setText("Total: " + current_cart.getTotalPrice() + "€");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
