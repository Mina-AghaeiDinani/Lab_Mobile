package com.example.lab2firebase;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class CurrentOrders extends AppCompatActivity implements OrdersFragment.OrdersFragmentListener {
    ListView orderListView;

    ArrayList<Order> orderList;
    ArrayList<ItemOrdered> itemList;
    ArrayList<CartInfo> cartList;


    private OrdersFragment fragment1;
    private DetailedItemFragment fragment2;

    private String myRestaurantID;


    DatabaseReference databaseOrder;
    private DatabaseReference databaseFoodOrdered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle saveState = savedInstanceState;


        orderList = new ArrayList<>();
        itemList = new ArrayList<>();
        cartList = new ArrayList<>();

        myRestaurantID =  FirebaseAuth.getInstance().getCurrentUser().getUid();


        /****** WE NOW CREATE THE VIEW DEPENDING ON THE DEVICE AND ORIENTATION *****/

        fragment1 = new OrdersFragment();
        fragment2 = new DetailedItemFragment();

        databaseOrder = FirebaseDatabase.getInstance().getReference()
                .child("OrderInfo");


        Log.d("START","ON START");

        databaseOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartList.clear();
                Log.d("RESTO ID", "id: "+myRestaurantID);

                // First, we read the db for each order
                    for (DataSnapshot orderSnap : dataSnapshot.getChildren()){
                        Log.d("ORDER SNAP", "order: "+orderSnap);
                        CartInfo cart = (CartInfo) orderSnap.getValue(CartInfo.class);
                        if(myRestaurantID.equals(cart.getRestaurantId())){ // We check that this Order is for our Restaurant
                            Log.d("INSIDE IF", "order: "+cart);
                            //If it is the case, we add it to the list we will display
                            cart.setOrderedId(orderSnap.getKey());
                            cartList.add(cart);

                        }
                    }

                int orientation = getResources().getConfiguration().orientation;
                Log.d("FRAG","FRAGMENTS CALLED");

                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    setContentView(com.example.lab2firebase.R.layout.activity_current_orders);
                    OrdersFragment f1 = (OrdersFragment) getSupportFragmentManager().findFragmentById(com.example.lab2firebase.R.id.fragment_container);

                    if(f1 != null && f1.isAdded()){
                        Log.d("FRAG","UPDATING FRAGMENTS");
                        Fragment frg = null;
                        frg = getSupportFragmentManager().findFragmentById(com.example.lab2firebase.R.id.fragment_container);
                        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();

                    }else {
                        Log.d("FRAG","CREATING FRAGMENTS");

                        if (findViewById(com.example.lab2firebase.R.id.fragment_container) != null) {
                            if (saveState != null) {
                                return;
                            }
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

}
