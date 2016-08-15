package br.com.trasmontano.trasmontanoassociadomobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class CentralDeAtendimentoActivity extends AppCompatActivity {

    private LinearLayout layAtendimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_de_atendimento);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        layAtendimento = (LinearLayout)findViewById(R.id.layAtendimento);

        layAtendimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CentralDeAtendimentoActivity.this, EmailActivity.class);
                startActivity(i);
            }
        });
    }
}
