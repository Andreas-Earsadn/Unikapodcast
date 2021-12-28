package id.bass.unikapodcast;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class adminupload extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private boolean checkPermission = false;
    Uri uri;// uri sama saja dengan URL
    String audioname;
    Button selectfile, upload;
    TextView notification;
    ProgressDialog progressDialog;
    FirebaseStorage storage; //used for uploading files ...
    FirebaseDatabase database;
    Spinner folderuploading;
    TextView txtrecord;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminupload);

        storage = FirebaseStorage.getInstance();// return an object of firebase Storage
        database = FirebaseDatabase.getInstance();
        folderuploading = findViewById(R.id.folderuploading);
        selectfile = findViewById(R.id.selectfilebutton);
        upload = findViewById(R.id.uploadbutton);
        notification = findViewById(R.id.notifications);
        txtrecord = findViewById(R.id.txtrecord);
        selectfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(adminupload.this, Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){
                    selectaudio();
                }
                else
                    ActivityCompat.requestPermissions(adminupload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uri!= null)
                    uploadFile(uri);
                else
                    Toast.makeText(adminupload.this,"Select a File", Toast.LENGTH_SHORT).show();
            }
        });

        txtrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recorder = new Intent(adminupload.this,recordingpage.class);
                startActivity(recorder);

                Toast.makeText(adminupload.this, "you can start recording your voice", Toast.LENGTH_SHORT).show();

            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.mytoolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("    U N I K A P O D C A S T");
        actionBar.getThemedContext();


        /*-------------Navigation Drawer Menu-------------*/

        //Hide or show items

        Menu menu =navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        //menu.findItem(R.id.nav_profile).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_home:
                break;

            case R.id.nav_unika:
                Toast.makeText(this, "Informasi Unika", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(adminupload.this, informasiunikast.class);
                startActivity(intent);//tambahkan informasi audio unikapodcast disini
                break;

            case R.id.search:
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
//                Intent adminupload = new Intent(HalamanUtama.this,adminupload.class);
//                startActivity(adminupload);
                break;

            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_gallery:
                Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:

                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(adminupload.this, MainActivity.class); //need firebase process
                startActivity(logout);
                finish();
                break;

            case R.id.nav_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                Intent profile = new Intent(adminupload.this, profilepod.class);
                startActivity(profile);
                break;

            case R.id.nav_aboutus:
                Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
                Intent aboutus = new Intent(adminupload.this, aboutus.class);
                startActivity(aboutus);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void uploadFile(Uri uri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file to database");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgress(0);
        progressDialog.show();
        // progressDialog.setCancelable(true);
//        progressDialog.dismiss();



        final String fileName = System.currentTimeMillis()+"";


        StorageReference storageReference= storage.getReference();//returns root path

        storageReference.child(folderuploading.getSelectedItem().toString()).child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.setMessage("finish");


                        String url = taskSnapshot.getStorage().getDownloadUrl().toString();//return the url that you uploaded file
                        // store the url in realtime database..
                        DatabaseReference reference = database.getReference();// return the path to the root

                        reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful())

                                    Toast.makeText(adminupload.this,"File  uploaded successfully", Toast.LENGTH_SHORT).show();

                                else
                                    Toast.makeText(adminupload.this, "File was not  uploaded successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(adminupload.this, "File was not uploaded successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                int currentProgress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);

            }
        });
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==9 &&  grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectaudio();
        }
        else
            Toast.makeText(adminupload.this,"please provide permission..", Toast.LENGTH_SHORT).show();
    }

    private void selectaudio() {

        // to offer user to select a file using file manager
        // we will be using an Intent

        Intent intent = new Intent();
        intent.setType("application/audio");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();// return the uri
            notification.setText("A file is selected :"+data.getData().getLastPathSegment());
        } else {
            Toast.makeText(adminupload.this, "Please select a file", Toast.LENGTH_SHORT).show();

        }
    }
}


