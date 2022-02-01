package com.example.task_rapidchidori_android.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task_rapidchidori_android.R;

public class SplashscreenActivity extends AppCompatActivity {

    ImageView imageView;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);
        imageView = findViewById(R.id.logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //This method will be executed once the timer is over
                // Start your app main activity
                animation = AnimationUtils.loadAnimation(SplashscreenActivity.this, R.anim.anim_effect);
                animation.setDuration(3000);
                imageView.startAnimation(animation);
                Intent i = new Intent(SplashscreenActivity.this, NotesActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }, 9000);
    }
}