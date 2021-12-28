package id.bass.unikapodcast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class HalamanUtama extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
DrawerLayout drawerLayout;
FirebaseAuth fAuth;
FirebaseFirestore fStore;
NavigationView navigationView;
GridView gridView;
CardView cardView;
String userId;
Button resendCode;
TextView verifyMsg;
private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama);
        resendCode = findViewById(R.id.resendCode);
        verifyMsg = findViewById(R.id.verifyMsg);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        FirebaseUser user = fAuth.getCurrentUser();

        if (user.isEmailVerified()) {
            resendCode.setVisibility(View.GONE);
            verifyMsg.setVisibility(View.GONE);
        }

            if (!user.isEmailVerified()) {
            resendCode.setVisibility(View.VISIBLE);
            verifyMsg.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = fAuth.getCurrentUser();
                    // kirim email verifikasi
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification Email Has been Sent", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag","on Failure: Email Was Not Sent" + e.getMessage());
                        }
                    });
                }
            });
        }
        /*-------------Hooks-------------*/

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.mytoolbar);



        /*-------------Tool Bar-------------*/

          setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        /*-------------Navigation Drawer Menu-------------*/

        //Hide or show items

        Menu menu =navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        //menu.findItem(R.id.nav_profile).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
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

        switch (item.getItemId()){

            case R.id.nav_home:
            break;

            case R.id.nav_unika:
                Toast.makeText(this, "Informasi Unika",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HalamanUtama.this,informasiunikast.class);
                startActivity(intent);//tambahkan informasi audio unikapodcast disini
                break;

            case R.id.search:
                Toast.makeText(this, "search",Toast.LENGTH_SHORT).show();
//                Intent adminupload = new Intent(HalamanUtama.this,adminupload.class);
//                startActivity(adminupload);
                break;

            case R.id.nav_share:
                Toast.makeText(this, "Share",Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_gallery:
                Toast.makeText(this, "Gallery",Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:

                Toast.makeText(this, "Logout",Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(HalamanUtama.this,MainActivity.class); //need firebase process
                startActivity(logout);
                finish();
                break;

            case R.id.nav_profile:
                Toast.makeText(this, "Profile",Toast.LENGTH_SHORT).show();
                Intent profile = new Intent(HalamanUtama.this,profilepod.class);
                startActivity(profile);
                break;

            case R.id.nav_aboutus:
                Toast.makeText(this, "About Us",Toast.LENGTH_SHORT).show();
                Intent aboutus = new Intent(HalamanUtama.this,aboutus.class);
                startActivity(aboutus);
                break;
        }

    drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}