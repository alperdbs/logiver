package com.alpertunademirbas.nakile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import android.content.Intent;
import android.os.Handler;

import androidx.appcompat.app.AppCompatDelegate;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // 2 saniye
    private static final float SCALE_FROM = 1.0f;
    private static final float SCALE_TO = 1.4f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(getResources().getColor(android.R.color.black));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        final ImageView splashImage = findViewById(R.id.imageView);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                SCALE_FROM, SCALE_TO,
                SCALE_FROM, SCALE_TO,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(SPLASH_DURATION);
        splashImage.startAnimation(scaleAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainScreenActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);

    }
}
