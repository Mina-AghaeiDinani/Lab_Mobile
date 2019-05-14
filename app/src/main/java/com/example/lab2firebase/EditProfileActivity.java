package com.example.lab2firebase;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class EditProfileActivity extends AppCompatActivity {

    EditText tvName,tvNameRestaurant,tvMail,tvPhone,tvLocalPhone,tvDescription;
    EditText tvMonday,tvTuesday,tvWednesday,tvThursday,tvFriday,tvSaturday,tvSunday;
    de.hdodenhof.circleimageview.CircleImageView imgProfile;
    private ProgressBar progressBar;
    private Button btn_Confirm,btn_Back;
    //*********Define variables to read from camera and put in ImageView
    private String current_image_uri;
    private Uri image_uri;
    private Bitmap imageBitmap;
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;
    //...
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.lab2firebase.R.layout.activity_edit_profile);

        //......
        //..authentication
        firebaseAuth=FirebaseAuth.getInstance();
        //reading from data base
        firebaseDatabase=FirebaseDatabase.getInstance();

        //define ids
        tvName=findViewById(com.example.lab2firebase.R.id.edt_Editname);
        tvNameRestaurant=findViewById(com.example.lab2firebase.R.id.edt_EditnameRest);
        tvMail=findViewById(com.example.lab2firebase.R.id.edt_Editmail);
        tvPhone=findViewById(com.example.lab2firebase.R.id.edt_EditCellPhone);
        tvLocalPhone=findViewById(com.example.lab2firebase.R.id.edt_EditphoneRest);
        tvDescription=findViewById(com.example.lab2firebase.R.id.edt_Editdescription);
        imgProfile=findViewById(com.example.lab2firebase.R.id.imgEditPro);

        tvMonday=findViewById(com.example.lab2firebase.R.id.edt_EditMonday);
        tvTuesday=findViewById(com.example.lab2firebase.R.id.edt_EditTuesday);
        tvWednesday=findViewById(com.example.lab2firebase.R.id.edt_EditWednesday);
        tvThursday=findViewById(com.example.lab2firebase.R.id.edt_EditThursday);
        tvFriday=findViewById(com.example.lab2firebase.R.id.edt_EditFriday);
        tvSaturday=findViewById(com.example.lab2firebase.R.id.edt_EditSaturday);
        tvSunday=findViewById(com.example.lab2firebase.R.id.edt_EditSunday);

        //get reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants");
        databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ///reading from data base and showing in views
                RestaurantsProfile restaurantsProfile=dataSnapshot.getValue(RestaurantsProfile.class);
                tvName.setText(restaurantsProfile.getName());
                tvNameRestaurant.setText(restaurantsProfile.getNamerestaurant());
                tvPhone.setText(restaurantsProfile.getPhone());
                tvLocalPhone.setText(restaurantsProfile.getPhonerestaurant());
                tvMail.setText(restaurantsProfile.getEmail());
                tvDescription.setText(restaurantsProfile.getDescription());
                current_image_uri=restaurantsProfile.getImageUrl();
                Picasso.get()
                        .load(restaurantsProfile.getImageUrl())
                        .placeholder(com.example.lab2firebase.R.drawable.personal)
                        .fit()
                        .centerCrop()
                        .into(imgProfile);


                tvMonday.setText(restaurantsProfile.getMonday());
                tvTuesday.setText(restaurantsProfile.getTuesday());
                tvWednesday.setText(restaurantsProfile.getWednesday());
                tvThursday.setText(restaurantsProfile.getThursday());
                tvFriday.setText(restaurantsProfile.getFriday());
                tvSaturday.setText(restaurantsProfile.getSaturday());
                tvSunday.setText(restaurantsProfile.getSunday());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this,databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        });

        //
        //****************************** Camera
        ImageButton btnSelectPhoto=findViewById(com.example.lab2firebase.R.id.btnEditPro);
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });
        // End

        //
        btn_Confirm = findViewById(com.example.lab2firebase.R.id.EditbtnRegProfile);
        btn_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();

            }
        });
        //
        btn_Back=findViewById(com.example.lab2firebase.R.id.EditbtnBack);
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditProfileActivity.this,ProfileActivity.class);
                startActivity(intent);
                finish();

            }
        });



    }


    // *****************Camera
    // *****************This part create dialog box
    private void selectImage() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    openCamera();
                } else if (options[item].equals("Choose from Gallery")) {
                    openGallery();
                } else if (options[item].equals("Delete")) {
                    int drawableResource = com.example.lab2firebase.R.drawable.personal;
                    Drawable d = getResources().getDrawable(drawableResource);
                    imgProfile.setImageDrawable(d);

                    dialog.dismiss();
                }
            }
        });


        builder.show();
    }

    //..........................
    //*****Open Gallery
    public void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    //.....................

    //***********Open Camera
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //........................
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            image_uri = data.getData();
            imgProfile.setImageURI(image_uri);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            image_uri = getImageUri(this, imageBitmap);
            imgProfile.setImageURI(image_uri);
        }
    }

    //..................
    //*****
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    //..................
    //******* We want when we rotate screen image does not change
    //We use these 2 below fuctions
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (image_uri != null) {
            outState.putString("image", image_uri.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String image = savedInstanceState.getString("image", ""); // Value that was saved will restore to variable
        image_uri = Uri.parse(image);
        imgProfile.setImageURI(image_uri);
    }

    //....................................
    //.............
    private void updateUser(){
        final String name = tvName.getText().toString().trim();
        final String email = tvMail.getText().toString().trim();
        final String phone = tvPhone.getText().toString().trim();

        final String monday = tvMonday.getText().toString().trim();
        final String tuesday = tvTuesday.getText().toString().trim();
        final String wednesday = tvWednesday.getText().toString().trim();
        final String thursday = tvThursday.getText().toString().trim();
        final String friday = tvFriday.getText().toString().trim();
        final String saturday = tvSaturday.getText().toString().trim();
        final String sunday = tvSunday.getText().toString().trim();

        final String nameRest = tvNameRestaurant.getText().toString().trim();
        final String phoneRest = tvLocalPhone.getText().toString().trim();
        final String description = tvDescription.getText().toString().trim();

        RestaurantsProfile restaurantsProfile = new RestaurantsProfile();
        restaurantsProfile.setName(name);
        restaurantsProfile.setEmail(email);
        restaurantsProfile.setPhone(phone);


        restaurantsProfile.setMonday(monday);
        restaurantsProfile.setThursday(thursday);
        restaurantsProfile.setTuesday(tuesday);
        restaurantsProfile.setWednesday(wednesday);
        restaurantsProfile.setFriday(friday);
        restaurantsProfile.setSaturday(saturday);
        restaurantsProfile.setSunday(sunday);

        restaurantsProfile.setNamerestaurant(nameRest);
        restaurantsProfile.setPhonerestaurant(phoneRest);
        restaurantsProfile.setDescription(description);

        if (image_uri!=null){
            restaurantsProfile.setImageUrl(String.valueOf(image_uri));
        }
        else restaurantsProfile.setImageUrl(current_image_uri);
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants");
        databaseReference.child(firebaseAuth.getUid()).setValue(restaurantsProfile);
        finish();


    }

}
