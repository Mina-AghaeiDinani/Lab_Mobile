package com.example.lab2firebase;

public class OrderdFood {
    //Property name must be the same as what we defined in real time database
    private String foodId, foodName, foodPrice, comment, number, totalPrice;


    public OrderdFood() {
        //Constructor , it is needed
    }

    public OrderdFood(String foodId, String foodName, String foodPrice, String comment
            , String number, String totalPrice
    ) {
        this.totalPrice = totalPrice;
        this.foodPrice = foodPrice;
        this.foodName = foodName;
        this.foodId = foodId;
        this.comment = comment;
        this.number = number;


    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}