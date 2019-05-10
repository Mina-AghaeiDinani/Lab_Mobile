package com.example.lab2firebase;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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


    private OrdersFragment fragment1;
    private DetailedItemFragment fragment2;


    DatabaseReference databaseOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle saveState = savedInstanceState;


        orderList = new ArrayList<>();
        itemList = new ArrayList<>();


        /****** WE NOW CREATE THE VIEW DEPENDING ON THE DEVICE AND ORIENTATION *****/

        fragment1 = new OrdersFragment();
        fragment2 = new DetailedItemFragment();

        databaseOrder = FirebaseDatabase.getInstance().getReference().child("Order");

        Log.d("START","ON START");

        databaseOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order orders = (Order) orderSnapshot.getValue(Order.class);
                    ArrayList<Object> itemsOrdered = (ArrayList<Object>) orderSnapshot.child("allItems").getValue();
                    Log.d("ITEMLIST1","ON DATA CHANGE"+itemList);
                    Log.d("ORDERZZ","ON DATA CHANGE"+orders);

                    itemList.clear();

                    for(Object itemsObj : itemsOrdered){
                        Map<String, Object> itemOrdered = (Map<String, Object>) itemsObj;
                        ItemOrdered item = new ItemOrdered((String) itemOrdered.get("name"), (Double) Double.parseDouble(itemOrdered.get("price").toString()), (Integer) Integer.parseInt(itemOrdered.get("quantity").toString()), (Double) Double.parseDouble(itemOrdered.get("discount").toString()), (String) itemOrdered.get("description"));
                        itemList.add(item);
                        Log.d("ITEMLIST2","ON DATA CHANGE"+itemList);

                    }
                    Log.d("ITEMLIST3","ON DATA CHANGE"+itemList);
                    orders.setItems(itemList);
                    orderList.add(orders);

                }
                //OrderAdapter orderAdapter = new OrderAdapter(getApplicationContext(), orderList);
                //orderListView.setAdapter(orderAdapter);

                int orientation = getResources().getConfiguration().orientation;
                Log.d("FRAG","FRAGMENTS CALLED");

                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    setContentView(com.example.lab2firebase.R.layout.activity_current_orders);
                    if (findViewById(com.example.lab2firebase.R.id.fragment_container)!= null){
                        if (saveState != null){
                            return;
                        }
                        getSupportFragmentManager().beginTransaction()
                                .add(com.example.lab2firebase.R.id.fragment_container, fragment1)
                                .commit();
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
            fragment2.updateView((Order) detailed_order);
            getSupportFragmentManager().beginTransaction().replace(com.example.lab2firebase.R.id.orderDetail_fragment, fragment2)
                    .commit();
        }else{
            //fragment2.updateView((Order) detailed_order);
            Intent intent_act2 = new Intent(getApplicationContext(), DetailedItemActivity.class);
            intent_act2.putExtra("Order_clicked", (Order) detailed_order);
            startActivity(intent_act2);
        }



    }

    public ArrayList<Order> getOrders(){
        Log.d("order","GET ORDERS CALLED");

        return orderList;
    }

}
