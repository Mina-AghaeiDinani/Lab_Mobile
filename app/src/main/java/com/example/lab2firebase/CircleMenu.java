package com.example.lab2firebase;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.google.firebase.auth.FirebaseAuth;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

public class CircleMenu extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    String names[]= {"Your Profile","Edit Profile","Add Daily Offers","Delete Daily Offers"};
    com.hitomi.cmlibrary.CircleMenu circleMenu;
    public Integer action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.lab2firebase.R.layout.activity_circle_menu);
        firebaseAuth=FirebaseAuth.getInstance();
        action=0;
        circleMenu =  findViewById(com.example.lab2firebase.R.id.circleMenu);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), com.example.lab2firebase.R.drawable.add, com.example.lab2firebase.R.drawable.remove)
                .addSubMenu(Color.parseColor("#258CFF"), com.example.lab2firebase.R.drawable.notification)
                .addSubMenu(Color.parseColor("#30A400"), com.example.lab2firebase.R.drawable.hat)
                .addSubMenu(Color.parseColor("#FF4B32"), com.example.lab2firebase.R.drawable.add_menu)
                .addSubMenu(Color.parseColor("#8A39FF"), com.example.lab2firebase.R.drawable.sign_out)
                .addSubMenu(Color.parseColor("#D8A104"), com.example.lab2firebase.R.drawable.view_icon)
                .addSubMenu(Color.parseColor("#D81B60"), com.example.lab2firebase.R.drawable.help)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index) {

                        if (index==1)
                        {
                            action=1;
                        }else if (index==2)
                        {
                            action=2;
                        }else if (index==3)
                        {
                        action=3;
                        }else if (index==4)
                        {
                            action=4;
                        }
                        else if (index==5)
                        {
                            action=5;
                        }
                        else if (index==6)
                        {
                            action=6;
                        }
                    }

                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

            @Override
            public void onMenuOpened() {}

            @Override
            public void onMenuClosed() {
                if (action==1) {
                    Intent intent = new Intent(CircleMenu.this, ProfileActivity.class);
                    startActivity(intent);
                }
                if (action==2) {
                    Intent intent = new Intent(CircleMenu.this, NewDailyOffer.class);
                    startActivity(intent);
                }
                if (action==3) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(CircleMenu.this,LoginActivity.class));
                }
                if (action==4) {
                    Intent intent = new Intent(CircleMenu.this, FoodListActivity.class);
                    startActivity(intent);
                }
                if (action==0) {
                    Intent intent = new Intent(CircleMenu.this, CurrentOrders.class);
                    startActivity(intent);
                }

            }

        });


    }
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        circleMenu.openMenu();
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onBackPressed() {
        circleMenu.closeMenu();
    }
    @Override
    protected void onPause(){
        super.onPause();
        action=-1;
    }
}
