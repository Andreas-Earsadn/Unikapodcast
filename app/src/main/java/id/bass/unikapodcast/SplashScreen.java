package id.bass.unikapodcast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       new Handler (Looper.getMainLooper()).postDelayed(new Runnable() {
           @Override
           public void run() {

                   startActivity(new Intent(getApplicationContext(),MainActivity.class));
                   finish();
               }
           },3000L);


    }
}
