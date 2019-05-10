package com.example.lab2firebase;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    ArrayList<Order> totalorders;

    public OrderAdapter(Context c, ArrayList<Order> totalorders){
        this.totalorders = totalorders;
        mInflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return totalorders.size();
    }

    @Override
    public Object getItem(int position) {
        return totalorders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return totalorders.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Order currentOrder = totalorders.get(position);
        Log.d("ADAPTER2","GET ORDER CALLED"+currentOrder.getAllItems());

        View v = mInflater.inflate(com.example.lab2firebase.R.layout.order_list, null);
        TextView orderTextView = (TextView) v.findViewById(com.example.lab2firebase.R.id.OrderTextView);
        TextView nbitemTextView = (TextView) v.findViewById(com.example.lab2firebase.R.id.nbitemTextView);
        TextView priceTextView = (TextView) v.findViewById(com.example.lab2firebase.R.id.priceTextView);
        TextView customerTextView = (TextView) v.findViewById(com.example.lab2firebase.R.id.customerTextView);

        String order = "Order n°"+currentOrder.getId();
        String nbitem = ""+currentOrder.getNbItem()+" items";
        String price = ""+currentOrder.getTotalPrice()+"€";
        String customer = currentOrder.getCustomer();

        orderTextView.setText(order);
        nbitemTextView.setText(nbitem);
        priceTextView.setText(price);
        customerTextView.setText(customer);

        return v;
    }
}
