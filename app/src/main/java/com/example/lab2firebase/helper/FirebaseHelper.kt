package com.example.lab2firebase.helper

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.lab2firebase.model.Driver
import com.google.firebase.auth.FirebaseAuth

class FirebaseHelper constructor(customerId: String) {
    private  val  uId: String? = FirebaseAuth.getInstance().currentUser!!.uid


    companion object {
        private const val ONLINE_CUSTOMERS = "RestaurantsLocation"

    }

    private val onlineCustomerDatabaseReference: DatabaseReference = FirebaseDatabase
            .getInstance()
            .reference
            .child(ONLINE_CUSTOMERS)
            .child(uId.toString())

    //init {
    //    onlineDriverDatabaseReference
      //          .onDisconnect()
        //        .removeValue()
    //}

    fun updateDriver(driver: Driver) {
        onlineCustomerDatabaseReference
                .setValue(driver)
        Log.e("Customer Info", " Updated")
    }

    /*fun deleteDriver() {
        onlineDriverDatabaseReference
                .removeValue()
    }*/
}