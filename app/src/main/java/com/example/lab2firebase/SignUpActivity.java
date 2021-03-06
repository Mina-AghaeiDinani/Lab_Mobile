package com.example.lab2firebase;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.os.Handler;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class SignUpActivity extends AppCompatActivity {
    private EditText txt_Name, txt_Mail, txt_Phone, txt_Password;
    private de.hdodenhof.circleimageview.CircleImageView imgProfile;
    private EditText txt_Description, txt_NameRest, txt_PhoneRest;
    private EditText Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    //*********Define variables to read from camera and put in ImageView
    private Uri image_uri;
    ImageButton btnSelectPhoto;
    private Bitmap imageBitmap;
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;
    // End
    private Button btn_Confirm;
    private TextView tvRegisterd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.lab2firebase.R.layout.activity_sign_up);
        setupUIViews();
        //****************************** Camera
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });
        // End
        progressBar.setVisibility(View.GONE);
        //Authentication
        mAuth = FirebaseAuth.getInstance();
        btn_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);

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


    //**** To make it more clean we assign variable to each view here
    private void setupUIViews() {
        txt_Name = findViewById(com.example.lab2firebase.R.id.edt_name);
        txt_Password = findViewById(com.example.lab2firebase.R.id.edt_password);
        txt_Mail = findViewById(com.example.lab2firebase.R.id.edt_mail);
        txt_Phone = findViewById(com.example.lab2firebase.R.id.edt_cellPhone);
        imgProfile = findViewById(com.example.lab2firebase.R.id.imgProfile);

        txt_NameRest = findViewById(com.example.lab2firebase.R.id.edt_nameRest);
        txt_PhoneRest = findViewById(com.example.lab2firebase.R.id.edt_phoneRest);
        txt_Description = findViewById(com.example.lab2firebase.R.id.edt_description);

        Monday = findViewById(com.example.lab2firebase.R.id.edt_Monday);
        Tuesday = findViewById(com.example.lab2firebase.R.id.edt_Tuesday);
        Wednesday = findViewById(com.example.lab2firebase.R.id.edt_Wednesday);
        Thursday = findViewById(com.example.lab2firebase.R.id.edt_Thursday);
        Friday = findViewById(com.example.lab2firebase.R.id.edt_Friday);
        Saturday = findViewById(com.example.lab2firebase.R.id.edt_Saturday);
        Sunday = findViewById(com.example.lab2firebase.R.id.edt_Sunday);

        progressBar = findViewById(com.example.lab2firebase.R.id.progressbar);
        btn_Confirm = findViewById(com.example.lab2firebase.R.id.btnRegProfile);
        tvRegisterd = findViewById(com.example.lab2firebase.R.id.tvReg);
        btnSelectPhoto = findViewById(com.example.lab2firebase.R.id.btnPhotoProfile);

    }

    // Saving information in real time database and doing authentication
    private void registerUser() {
        final String name = txt_Name.getText().toString().trim();
        final String email = txt_Mail.getText().toString().trim();
        final String password = txt_Password.getText().toString().trim();
        final String phone = txt_Phone.getText().toString().trim();

        final String monday = Monday.getText().toString().trim();
        final String tuesday = Tuesday.getText().toString().trim();
        final String wednesday = Wednesday.getText().toString().trim();
        final String thursday = Thursday.getText().toString().trim();
        final String friday = Friday.getText().toString().trim();
        final String saturday = Saturday.getText().toString().trim();
        final String sunday = Sunday.getText().toString().trim();

        final String nameRest = txt_NameRest.getText().toString().trim();
        final String phoneRest = txt_PhoneRest.getText().toString().trim();
        final String description = txt_Description.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        btn_Confirm.setEnabled(false);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                            addInfo();
                            /*RestaurantsProfile restaurantsProfile = new RestaurantsProfile();
                            restaurantsProfile.setName(name);
                            restaurantsProfile.setEmail(email);
                            restaurantsProfile.setPhone(phone);
                            restaurantsProfile.setImageUrl(String.valueOf(image_uri));

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


                            FirebaseDatabase.getInstance().getReference("Restaurants")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(restaurantsProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Toast.makeText(SignUpActivity.this, "Registration has been done!", Toast.LENGTH_LONG).show();
                                    } else {
                                        // display a failure message
                                    }
                                }
                            });*/

                        } else {
                            //If email has been already registered
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignUpActivity.this, "You are already registered!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    //
    private void addInfo() {
        Toast.makeText(SignUpActivity.this, "Please wait...", Toast.LENGTH_LONG).show();
        mStorageRef = FirebaseStorage.getInstance().getReference("Restaurants");
        if (image_uri != null) {
            final StorageReference fileReferences = mStorageRef.child(System.currentTimeMillis() + "." + getExtension(image_uri));
            mUploadTask = fileReferences.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);
                            fileReferences.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    RestaurantsProfile restaurantsProfile = new RestaurantsProfile(
                                            txt_Name.getText().toString().trim(),
                                            txt_Phone.getText().toString().trim(),
                                            txt_Mail.getText().toString().trim(),
                                            txt_NameRest.getText().toString().trim(),
                                            txt_PhoneRest.getText().toString().trim(),
                                            txt_Description.getText().toString().trim(),
                                            uri.toString(),
                                    Monday.getText().toString().trim(),
                                    Tuesday.getText().toString().trim(),
                                    Wednesday.getText().toString().trim(),
                                    Thursday.getText().toString().trim(),
                                    Friday.getText().toString().trim(),
                                    Saturday.getText().toString().trim(),
                                    Sunday.getText().toString().trim(),
                                    0, 0);
                                    FirebaseDatabase.getInstance().getReference("Restaurants")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(restaurantsProfile);


                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });

        } else
            Toast.makeText(SignUpActivity.this, "no photo has been selected", Toast.LENGTH_LONG).show();


    }
}
