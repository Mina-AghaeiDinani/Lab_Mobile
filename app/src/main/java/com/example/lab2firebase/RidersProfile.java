package com.example.lab2firebase;

public class RidersProfile {
    //Property name must be the same as what we defined in real time database
    private String name, phone, email ,imageUrl, shortdescription;
    public RidersProfile() {
        //Constructor , it is needed
    }

    public RidersProfile(String name, String phone, String email, String shortdescription, String imageUrl) {
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.name = name;
        this.email = email;
        this.shortdescription = shortdescription;

        if (shortdescription.trim().equals("")) {
            this.shortdescription = "Information is not provided";
        } else this.shortdescription = shortdescription;

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


    public String getShortdescription() {
        return shortdescription;
    }

    public void setShortdescription(String description) {
        this.shortdescription = description;
    }

}
