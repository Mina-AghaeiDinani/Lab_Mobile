package com.example.lab2firebase;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private int id;
    private int nbItem;
    private double totalPrice;
    private String customer;
    private String comments;
    private ArrayList<ItemOrdered> items;

    public Order(){}

    public Order(int id, String customer, String comments, ArrayList<ItemOrdered> items){
        this.id = id;
        this.comments = comments;
        this.customer = customer;
        this.items = items;
    }

    public Order(int id, String customer, ArrayList<ItemOrdered> items){
        this.id = id;
        this.comments = "";
        this.customer = customer;
        this.items = items;
    }

    public int getId(){
        return id;
    }

    public double getTotalPrice(){
        totalPrice = 0.0;
        int i;
        for (i=0; i<items.size();i++){
            totalPrice += (items.get(i).getPrice())*(items.get(i).getQuantity());
        }

        totalPrice = (double)(Math.round(totalPrice*100)) / 100;
        return totalPrice;
    }

    public int getNbItem(){
        nbItem = 0;
        int i;
        ArrayList<ItemOrdered> castedItem = (ArrayList<ItemOrdered>) items;
        for (i=0; i<castedItem.size();i++){
            Log.d("ORDER","GET ORDER CALLED"+castedItem);

            nbItem += castedItem.get(i).getQuantity();
        }
        return nbItem;
    }

    public String getCustomer(){
        return customer;
    }
    public String getComments(){
        return comments;
    }

    public ItemOrdered getItem(int position){
        return items.get(position);
    }

    public void setItems(ArrayList<ItemOrdered> items){
        this.items = items;
    }

    public ArrayList<ItemOrdered> getAllItems(){
        return items;
    }



}
