package com.example.lab2firebase;

import android.content.Intent;
import android.content.res.Configuration;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class CurrentOrders extends AppCompatActivity implements OrdersFragment.OrdersFragmentListener,NotificationDialogActivity.NotificationDialogListener {

    ArrayList<CartInfo> cartList;


    private OrdersFragment fragment1;
    private DetailedItemFragment fragment2;

    private String myRestaurantID;


    DatabaseReference databaseOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle saveState = savedInstanceState;

        Intent notificationIntent = getIntent();
        String notification_order_id = notificationIntent.getStringExtra("order_id");


        cartList = new ArrayList<>();

        myRestaurantID =  FirebaseAuth.getInstance().getCurrentUser().getUid();

        //If we receive a notification, we have to handle it as a priority:
        if(notification_order_id != null){
            openNotificationDialog(notification_order_id);
            Log.d("NOTI", "id:"+ notification_order_id);
        }

        /****** WE NOW CREATE THE VIEW DEPENDING ON THE DEVICE AND ORIENTATION *****/

        fragment1 = new OrdersFragment();
        fragment2 = new DetailedItemFragment();

        databaseOrder = FirebaseDatabase.getInstance().getReference()
                .child("OrderInfo");

        databaseOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartList.clear();

                // First, we read the db for each order
                    for (DataSnapshot orderSnap : dataSnapshot.getChildren()){
                        Log.d("ORDER SNAP", "order: "+orderSnap);
                        CartInfo cart = (CartInfo) orderSnap.getValue(CartInfo.class);

                        // We check that this Order is for our Restaurant and that it was accepted
                        if((myRestaurantID.equals(cart.getRestaurantId())) && (cart.getStatus().equals("accepted"))){
                            //If it is the case, we add it to the list we will display
                            cart.setOrderedId(orderSnap.getKey());
                            cartList.add(cart);
                        }
                    }
                displayFragments();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR","ERROR READING");
            }


        });







    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public void onOrderClicked(Object detailed_order) {
        DetailedItemFragment f2 = (DetailedItemFragment) getSupportFragmentManager().findFragmentById(com.example.lab2firebase.R.id.orderDetail_fragment);

        if(f2 != null && f2.isVisible()){
            fragment2.updateView((CartInfo) detailed_order);
            getSupportFragmentManager().beginTransaction().replace(com.example.lab2firebase.R.id.orderDetail_fragment, fragment2)
                    .commit();

        }else{
            //fragment2.updateView((Order) detailed_order);
            Intent intent_act2 = new Intent(getApplicationContext(), DetailedItemActivity.class);
            intent_act2.putExtra("Order_clicked", (CartInfo) detailed_order);
            startActivity(intent_act2);
        }



    }

    public ArrayList<CartInfo> getCartList(){
        Log.d("order","GET CARTS CALLED");

        return cartList;
    }

    public void openNotificationDialog(String order_id){ //Method called when a new element is added from the database
        NotificationDialogActivity notificationDialog = new NotificationDialogActivity();
        Bundle notificationBundle = new Bundle();
        notificationBundle.putString("new_orderID",order_id);
        Log.d("BUNDLE", "bundle:"+ notificationBundle);

        notificationDialog.setArguments(notificationBundle);
        notificationDialog.show(getSupportFragmentManager(), "Notification received");
    }

    @Override
    public void acceptOrder(String order) {
        databaseOrder = FirebaseDatabase.getInstance().getReference()
                .child("OrderInfo").child(order);

        databaseOrder.child("status").setValue("accepted");
        databaseOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CartInfo incomingOrder = dataSnapshot.getValue(CartInfo.class);
                incomingOrder.setOrderedId(dataSnapshot.getKey());

                cartList.add(incomingOrder);
                displayFragments();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", "ERROR READING");
            }
        });

        Toast.makeText(getApplicationContext(),"Order "+order+" Accepted", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void declineOrder(String order) {
        databaseOrder = FirebaseDatabase.getInstance().getReference()
                .child("OrderInfo").child(order);
        databaseOrder.child("status").setValue("declined");
        Toast.makeText(getApplicationContext(),"Order "+order+" Declined", Toast.LENGTH_SHORT).show();

    }

    public void displayFragments(){
        int orientation = getResources().getConfiguration().orientation;
        Log.d("FRAG","FRAGMENTS CALLED");

        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(com.example.lab2firebase.R.layout.activity_current_orders);
            OrdersFragment f1 = (OrdersFragment) getSupportFragmentManager().findFragmentById(com.example.lab2firebase.R.id.fragment_container);

            if (f1 != null && f1.isAdded()) {//We update the list displayed only if the data is one added for our restaurant
                Log.d("FRAG", "UPDATING FRAGMENTS");
                Fragment frg = null;
                frg = getSupportFragmentManager().findFragmentById(com.example.lab2firebase.R.id.fragment_container);
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            } else {
                Log.d("FRAG", "CREATING FRAGMENTS");

                if (findViewById(com.example.lab2firebase.R.id.fragment_container) != null) {

                    getSupportFragmentManager().beginTransaction()
                            .add(com.example.lab2firebase.R.id.fragment_container, fragment1)
                            .commit();
                }
            }
        }else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(com.example.lab2firebase.R.layout.activity_current_orders_landscape);
            getSupportFragmentManager().beginTransaction()
                    .replace(com.example.lab2firebase.R.id.orderTitle_fragment, fragment1)
                    .replace(com.example.lab2firebase.R.id.orderDetail_fragment, fragment2)
                    .commit();
        }
    }


}

