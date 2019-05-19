package com.example.lab2firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderdFoodAdapter extends BaseAdapter{
    LayoutInflater mInflater;
    ArrayList<OrderdFood> items;

    public OrderdFoodAdapter(Context c, ArrayList<OrderdFood> items){
        this.items = items;
        mInflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderdFood currentItem = items.get(position);
        View v = mInflater.inflate(com.example.lab2firebase.R.layout.detailed_item_list, null);
        TextView nameTextView = (TextView) v.findViewById(com.example.lab2firebase.R.id.nameTextView);
        TextView priceTextView = (TextView) v.findViewById(com.example.lab2firebase.R.id.priceTextView);
        TextView quantityTextView = (TextView) v.findViewById(com.example.lab2firebase.R.id.quantityTextView);

        String name = currentItem.getFoodName();
        String quantity = currentItem.getNumber();
        String price = currentItem.getFoodPrice()+"€";

        nameTextView.setText(name);
        priceTextView.setText(price);
        quantityTextView.setText(quantity);

        return v;
    }
}


