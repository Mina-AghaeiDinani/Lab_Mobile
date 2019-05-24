package com.example.lab2firebase;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CartInfoAdapter extends BaseAdapter{
    LayoutInflater mInflater;
    ArrayList<CartInfo> totalcarts;
    public CartInfoAdapter(Context c, ArrayList<CartInfo> totalcarts){
        this.totalcarts = totalcarts;
        mInflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return totalcarts.size();
    }

    @Override
    public Object getItem(int position) {
        return totalcarts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CartInfo currentCart = totalcarts.get(position);

        View v = mInflater.inflate(com.example.lab2firebase.R.layout.order_list, null);
        TextView orderTextView = (TextView) v.findViewById(com.example.lab2firebase.R.id.OrderTextView);
        TextView nbitemTextView = (TextView) v.findViewById(com.example.lab2firebase.R.id.nbitemTextView);
        TextView priceTextView = (TextView) v.findViewById(com.example.lab2firebase.R.id.priceTextView);
        TextView customerTextView = (TextView) v.findViewById(com.example.lab2firebase.R.id.customerTextView);

        String order = "Order "+currentCart.getOrderNum();
        String nbitem = ""+currentCart.getTotalItems()+" items";
        String price = ""+currentCart.getTotalPrice()+"€";
        String customer = currentCart.getCustomerName();

        orderTextView.setText(order);
        nbitemTextView.setText(nbitem);
        priceTextView.setText(price);
        customerTextView.setText(customer);

        return v;
    }
}
