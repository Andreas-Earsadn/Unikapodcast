package id.bass.unikapodcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class registeradmin extends Activity implements AdapterView.OnItemSelectedListener {
    EditText email, password, repassword, fullname, age;
    Spinner pendakhir, fakultas;
    Button kirim, gotologinadm;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
   private ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeradmin);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        gotologinadm = findViewById(R.id.btgotologin);
        email = findViewById(R.id.etemail);
        password = findViewById(R.id.etpass);
        repassword = findViewById(R.id.etretypepass);
        fullname = findViewById(R.id.etfullname);
        age = findViewById(R.id.etage);
        pendakhir = findViewById(R.id.spinpendidikan);
        fakultas = findViewById(R.id.spinfakultas);

        kirim = findViewById(R.id.btsend);

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkField(email);//memanggil fungsi untuk cek tiap field registrasi
                checkField(password);
                checkField(repassword);
                checkField(fullname);
                checkField(age);
               // checkField(pendakhir);
                //checkField(fakultas);

                if (valid) {
                    //untuk proses registrasi admin
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser admin = fAuth.getCurrentUser();

                            // kirim email verifikasi
                            admin.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(registeradmin.this, "Verification Email Has been Sent", Toast.LENGTH_SHORT).show();

                                }
                            });
                            Toast.makeText(registeradmin.this, "Your document is uploaded successfully", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fstore.collection("Users").document(admin.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("Email", email.getText().toString());
                            userInfo.put("Password", password.getText().toString());
                            userInfo.put("Fullname", fullname.getText().toString());
                            userInfo.put("Age", age.getText().toString());
                            userInfo.put("PendidikanAkhir", pendakhir.getSelectedItem().toString());
                            userInfo.put("Fakultas", fakultas.getSelectedItem().toString());

                            //spesifikasikan jika user adalah admin
                            userInfo.put("isAdmin", "1");

                            df.set(userInfo);


                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(registeradmin.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();


                        }
                    });

                }

            }


        });

        gotologinadm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

        public boolean checkField(EditText textfield ){

                if (textfield.getText().toString().isEmpty()){
                    textfield.setError("This Data is Required");
                    valid= false;
                } else {
                    valid = true;
                }return valid;
            }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}