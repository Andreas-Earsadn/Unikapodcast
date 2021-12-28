package id.bass.unikapodcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class registerpodcatcher extends Activity {
    EditText firstname, lastname, email, password;
    Button registerpc, gotologin;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpodcatcher);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.rgemail);
        password = findViewById(R.id.rgpassword);
        registerpc = findViewById(R.id.btregister);
        gotologin = findViewById(R.id.goTologin);

        registerpc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(firstname);//memanggil fungsi untuk cek tiap field registrasi
                checkField(lastname);
                checkField(email);
                checkField(password);

                if (valid) {
                    //untuk proses registrasi user podcatcher
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            // kirim email verifikasi
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(registerpodcatcher.this, "Verification Email Has been Sent", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"on Failure: Email Was Not Sent" + e.getMessage());
                                }
                            });
                            Toast.makeText(registerpodcatcher.this, "Your Account Has Been Created Successfully", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fstore.collection("Users").document(user.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("FirstName", firstname.getText().toString());
                            userInfo.put("LastName", lastname.getText().toString());
                            userInfo.put("UserEmail", email.getText().toString());
                            userInfo.put("Password", password.getText().toString());

                            //spesifikasikan jika user adalah admin
                            userInfo.put("isUser", "1");

                            df.set(userInfo);


                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(registerpodcatcher.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }



        });

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


}



    public boolean checkField(EditText textField){

        if (textField.getText().toString().isEmpty()){
            textField.setError("This Data is Required");
            valid= false;
        } else {
            valid = true;
        }return valid;
        }
    }
