package br.com.trasmontano.trasmontanoassociadomobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;



public class ActivitySplash extends AppCompatActivity {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageView = (ImageView)findViewById(R.id.imageView1);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash);
        imageView.startAnimation(animation);

       /* animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        imageView.startAnimation(animation);*/

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                finish();
               Intent intent = new Intent(ActivitySplash.this, MainActivity.class);
               startActivity(intent);

            }
        }, 7000);
    }
}
