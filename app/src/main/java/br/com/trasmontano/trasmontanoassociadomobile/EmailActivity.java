package br.com.trasmontano.trasmontanoassociadomobile;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;


public class EmailActivity extends AppCompatActivity {

    static String tipo;
    static String email;

    TextView tvSetor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tvSetor = (TextView)findViewById(R.id.tvSetor);

        Intent intent = getIntent();

        Bundle params = intent.getExtras();

        if (params != null) {
            tipo = params.getString("tipo");
            email = params.getString("tipo");
        }

        tvSetor.setText(tipo);






    }

}

