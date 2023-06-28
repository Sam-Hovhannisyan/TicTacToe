package com.samvel.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    TextView playOnline, playOffline, howToPlay;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        playOnline = findViewById(R.id.playOnline);
        playOffline = findViewById(R.id.playOffline);
        howToPlay = findViewById(R.id.howToPlay);

        playOnline.setOnClickListener(view -> playOnline.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce)));

        playOffline.setOnClickListener(view -> {
            playOffline.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce));
            changeActivity(MainActivity.class);
        });

        howToPlay.setOnClickListener(view -> howToPlay.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce)));
    }

    private void changeActivity(Class class_) {
        startActivity(new Intent(this, class_));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}