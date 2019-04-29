package com.example.lab2firebase;


public class DailyOffer {
    //Property name must be the same as what we defined in real time database
    private String name, price, discount, availablequantity, shortdescription;
    private String imageUrl,restaurantUid;
    public DailyOffer() {
        //Constructor , it is needed
    }

    public DailyOffer(String name, String price, String discount, String availablequantity, String shortdescription,String imageUrl,String restaurantUid) {
        if (discount.trim().equals("")) {
            this.discount = "0";
        } else this.discount = discount;
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.restaurantUid=restaurantUid;
        if (shortdescription.trim().equals("")) {
            this.shortdescription = "Information is not provided";
        } else this.shortdescription = shortdescription;
        this.availablequantity = availablequantity;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }
    public void setDiscount(String discount) {
        this.discount = discount;
    }


    public String getRestaurantUid() {
        return restaurantUid;
    }

    public void setRestaurantUid(String restaurantUid) {
        this.restaurantUid = restaurantUid;
    }

    public String getAvailablequantity() {
        return availablequantity;
    }
    public void setAvailablequantity(String availablequantity) {
        this.availablequantity = availablequantity;
    }

    public String getShortdescription() {
        return shortdescription;
    }
    public void setShortdescription(String shortdescription) {
        this.shortdescription = shortdescription;
    }

    public String getImageUrl() {
        return imageUrl;
   }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}