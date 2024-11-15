package com.bit.bookcab;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import utils.PreferenceHelper;

public class SplashScreen extends AppCompatActivity {
    LottieAnimationView animationView;
    float progress= (float) 0.1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);



        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3500);
//                  sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {



                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
String usertype= PreferenceHelper.getUsertype(SplashScreen.this);
if(usertype.equals("D")){
    Intent i = new Intent(SplashScreen.this, DriverHome.class);
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear all previous activities
    startActivity(i);
    SplashScreen.this.finish();
}else{
    Intent i = new Intent(SplashScreen.this, CustomerHome.class);
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear all previous activities
    startActivity(i);
    SplashScreen.this.finish();
}


                    } else {
                        // No user is signed in.

                        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear all previous activities
                        startActivity(i);
                        SplashScreen.this.finish();
                    }

                }
            }
        };
        timer.start();

    }



}

