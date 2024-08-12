package lk.zeamac.app.handicraftadmin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import lk.zeamac.app.handicraftadmin.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CeylonHandicraft_FullScreen);
        setContentView(R.layout.activity_splash);


        ImageView imageView = findViewById(R.id.splashLog);

        Picasso.get().load(R.drawable.logo)
                .resize(700,700)
                .into(imageView);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.splashProgressBar).setVisibility(View.VISIBLE);
            }
        },600);


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.splashProgressBar).setVisibility(View.INVISIBLE);


                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                    finish();


            }
        },8000);







    }

}