package id.bass.unikapodcast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class profilepod extends AppCompatActivity {
        TextView changeprofile, phone_textview,nameTextView, StatusTextView;
     TextView email_textview, instagram_textview, fb_textview;
     ImageView emailImageView, phoneImageView,  fbImageView,instagramImageView;
     Button buttonclose, buttonsave;
     //String email, password;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    ImageView userImageView;
    Uri imageUri;
    private  String myUri="";
    private StorageTask uploadTask;
    private StorageReference storageReferencePP;
    private DatabaseReference reference;
 String userId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);


        changeprofile = findViewById(R.id.changeprofilTV);
        nameTextView = findViewById(R.id.nameTV);
        StatusTextView = findViewById(R.id.statusTV);
        email_textview = findViewById(R.id.email_textView);
        instagram_textview = findViewById(R.id.instagram_textView);
        fb_textview = findViewById(R.id.fb_textView);
        phone_textview = findViewById(R.id.phone_textView);
        userImageView = findViewById(R.id.UserImageView);
        emailImageView = findViewById(R.id.email_imageview);
        instagramImageView = findViewById(R.id.instagram_imageview);
        fbImageView = findViewById(R.id.fb_imageview);
        buttonclose = findViewById(R.id.buttonclose);
        buttonsave = findViewById(R.id.buttonsave);


        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();



        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        //---hapus
        storageReferencePP = FirebaseStorage.getInstance().getReference().child("Users Profile Picture/"+fAuth.getCurrentUser().getUid()+"/Profile.jpg");
        StorageReference profileRef = storageReferencePP.child("Users Profile Picture/"+fAuth.getCurrentUser().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(userImageView);
            }
        });
//---------------
        buttonclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profilepod.this, HalamanUtama.class));
                finish();

            }
        });

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadProfileImage();


            }
        });

        changeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1, 1).start(profilepod.this);
            }
        });





        DocumentReference documentReference = fstore.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                nameTextView.setText(documentSnapshot.getString("FirstName"));
                StatusTextView.setText(documentSnapshot.getString("Status"));
                email_textview.setText(documentSnapshot.getString("UserEmail"));
                phone_textview.setText(documentSnapshot.getString("Phone"));
                instagram_textview.setText(documentSnapshot.getString("Instagram"));
                fb_textview.setText(documentSnapshot.getString("Facebook"));
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            imageUri = result.getUri();
            userImageView.setImageURI(imageUri);
        }
        else{
            Toast.makeText(this, "Error, Try again", Toast.LENGTH_SHORT).show();

        }
    }

    private void uploadProfileImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set Your Profile");
        progressDialog.setMessage("Please wait, while we are setting your data");
        progressDialog.show();

        if(imageUri != null)
        {
            final StorageReference fileRef = storageReferencePP
                    .child("Users Profile Picture/"+fAuth.getCurrentUser().getUid()+"/Profile.jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {

                        Uri downloadUrl =  task.getResult();
                        myUri =downloadUrl.toString();

                        HashMap<String, Object> userMap  = new HashMap<>();
                        userMap.put("image",myUri);

                        reference.child(fAuth.getCurrentUser().getUid()).updateChildren(userMap);


                        progressDialog.dismiss();

                    }
                }

            });
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }
}