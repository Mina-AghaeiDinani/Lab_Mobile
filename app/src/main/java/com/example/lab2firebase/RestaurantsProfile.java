package com.example.lab2firebase;

public class RestaurantsProfile {
    //Property name must be the same as what we defined in real time database
    private String name, phone, email ,imageUrl;
    private String namerestaurant,phonerestaurant,address,description;
    private String monday,tuesday,wednesday,thursday,friday,saturday,sunday;
    public RestaurantsProfile() {
        //Constructor , it is needed
    }

    public RestaurantsProfile(String name, String phone, String email,String namerestaurant,String phonerestaurant, String address, String description, String imageUrl,String monday,String tuesday,String wednesday,String thursday,String friday,String saturday,String sunday) {
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.name = name;
        this.email = email;


        this.address=address;
        this.namerestaurant=namerestaurant;
        this.phonerestaurant=phonerestaurant;
        if (description.trim().equals("")) {
            this.description = "Information is not provided";
        } else this.description = description;

        this.monday=monday;
        this.tuesday=tuesday;
        this.wednesday=wednesday;
        this.thursday=thursday;
        this.friday=friday;
        this.saturday=saturday;
        this.sunday=sunday;

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone =phone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    //Info of restaurant
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getNamerestaurant() {
        return namerestaurant;
    }

    public void setNamerestaurant(String namerestaurant) {
        this.namerestaurant = namerestaurant;
    }

    public String getPhonerestaurant() {
        return phonerestaurant;
    }

    public void setPhonerestaurant(String phonerestaurant) {
        this.phonerestaurant = phonerestaurant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }
}
