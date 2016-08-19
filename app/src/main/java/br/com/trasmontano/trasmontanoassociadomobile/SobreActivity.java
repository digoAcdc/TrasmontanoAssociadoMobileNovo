package br.com.trasmontano.trasmontanoassociadomobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SobreActivity extends AppCompatActivity {
private TextView tvTitulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tvTitulo = (TextView) findViewById(R.id.tvTitulo);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        tvTitulo.startAnimation(animation);


    }
}
