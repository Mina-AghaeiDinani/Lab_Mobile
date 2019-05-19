package com.example.lab2firebase;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailedItemFragment extends Fragment {
    @Nullable

    ListView itemsListView;
    TextView orderNumber;
    TextView totalPrice;
    Context thiscontext;
    protected Activity mActivity;
    private DatabaseReference databaseFoodOrdered;
    private String myRestaurantID;
    ArrayList<OrderdFood> items;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(com.example.lab2firebase.R.layout.detaileditemfragment_layout, container, false);

        itemsListView = (ListView) v.findViewById(com.example.lab2firebase.R.id.itemsListView);
        orderNumber = (TextView) v.findViewById(com.example.lab2firebase.R.id.orderNb_textView);
        totalPrice = (TextView) v.findViewById(com.example.lab2firebase.R.id.totalPrice_textView);

        myRestaurantID =  FirebaseAuth.getInstance().getCurrentUser().getUid();


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //thiscontext = (Context) getActivity();

    }

    public void updateView(CartInfo current_order){


        databaseFoodOrdered = FirebaseDatabase.getInstance().getReference("CartFoods")
                .child(current_order.getCustomerId())
                .child(myRestaurantID)
                .child("Foods");

        databaseFoodOrdered.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot foodSnap : dataSnapshot.getChildren()){
                    OrderdFood thisFood = (OrderdFood) foodSnap.getValue(OrderdFood.class);
                    items.add(thisFood);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(mActivity!=null) {
            OrderdFoodAdapter orderdFoodAdapterAdapter = new OrderdFoodAdapter(mActivity, items);
            itemsListView.setAdapter(orderdFoodAdapterAdapter);

            orderNumber.setText("Order n°" + current_order.getOrderedId());

            totalPrice.setText("Total: " + current_order.getTotalPrice() + "€");
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }


}
