package id.bass.unikapodcast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity  {
    EditText email, password;
    Button login;
    ProgressBar progressBar;
    // private ProgressDialog progressDialog;
    //private DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    //FirebaseDatabase firebaseDatabase;
    TextView registeruser, registeradmin;
    boolean valid = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth =FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();//pusing kepala karena lupa definisiin ini
        email = findViewById(R.id.etusername);
        password = findViewById(R.id.etpassword);
        progressBar =findViewById(R.id.progbar);
        login = findViewById(R.id.btlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(email);
                checkField(password);
                Log.d("TAG","onClick :"+ email.getText().toString());

                if(valid){
                    fAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            checkUserAccessLevel(authResult.getUser().getUid());
                            Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(), HalamanUtama.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    });

                }
            }
        });
        registeruser=(TextView)findViewById(R.id.linkregisterpodcatch);
        registeruser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registpc = new Intent(MainActivity.this,registerpodcatcher.class);
                startActivity(registpc);

                Toast.makeText(MainActivity.this, "you clicked on register", Toast.LENGTH_SHORT).show();
            }
        });
        //

        registeradmin=(TextView)findViewById(R.id.linkregistercalonadmin);
        registeradmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registca = new Intent(MainActivity.this,registeradmin.class);
                startActivity(registca);

                Toast.makeText(MainActivity.this, "you clicked on register", Toast.LENGTH_SHORT).show();
            }
        });

        //





    }

    private void checkUserAccessLevel(String uid) {

        DocumentReference df = fStore.collection("Users").document(uid);

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());

                //tes kondisi untuk identifikasi user admin atau podcatcher

                if (documentSnapshot.getString("isAdmin") != null) {

                    //user is admin



                    Intent isAdmin = new Intent(MainActivity.this,adminupload.class);
                    startActivity(isAdmin);
                    finish();
                }

                if(documentSnapshot.getString("isUser")!=null){
                    Intent isUser = new Intent(MainActivity.this,HalamanUtama.class);
                    startActivity(isUser);
                    finish();
                }

            }
        });
    }

    public boolean checkField(EditText textField) {

        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {

            valid = true;
        }
        return valid;

    }

//    @Override
//    protected void onStart(){
//        super.onStart();
////untuk melakukan check jika user signed in dan melakukan update//FirebaseUser currentUser = fAuth.getCurrentUser();
//        if(FirebaseAuth.getInstance().getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//            finish();
//        }
//
//
//    }
}