package com.example.conasforapp.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.conasforapp.R;

public class Main_Animacion extends AppCompatActivity {

    LottieAnimationView lottie,lottie2;

    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_animacion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgLogo = findViewById(R.id.imgLogoConasfor);
        lottie = findViewById(R.id.lottieAnimacion);
        lottie2 = findViewById(R.id.lottieAnimacion2);

        //imgLogo.animate().translationY(-1400).setDuration(2700).setStartDelay(0);
        //lottie.animate().translationX(2000).setDuration(2000).setStartDelay(2900);
        //lottie.setAnimation(R.raw.mainanimation);
        lottie.loop(true);
        lottie.playAnimation();
        lottie2.loop(true);
        lottie2.playAnimation();

        new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
             Intent intent = new Intent(Main_Animacion.this, MainActivity.class);
             startActivity(intent);
        }
        },5000);
    }
}