package com.example.lab2firebase;

import java.io.Serializable;

public class ItemOrdered implements Serializable {
    private double price;
    private String name;
    private int quantity;
    private double discount;
    private String description;

    public ItemOrdered(){}

    public ItemOrdered(String name, double price, int quantity, double discount, String description){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.description = description;
    }
    public ItemOrdered(String name, double price, int quantity){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discount = 0;
        this.description = "";
    }

    public String getName(){
        return name;
    }

    public double getPrice(){
        return price;
    }

    public int getQuantity(){
        return quantity;
    }

    public double getDiscount(){
        return discount;
    }

    public String getDescription(){
        return description;
    }

    public void setDiscount(double discount){
        this.discount = discount;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
}

