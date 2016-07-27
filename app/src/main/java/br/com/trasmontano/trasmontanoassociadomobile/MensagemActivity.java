package br.com.trasmontano.trasmontanoassociadomobile;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.Alarme;
import se.emilsjolander.sprinkles.Query;

public class MensagemActivity extends AppCompatActivity {
    ImageButton imbStop;
    TextView tvNmPaciente;
    TextView tvDescricaoMedicamento;
    TextView tvMedicamento;
    TextView tvFrequencia;
   // TextView tvHorarios;
    TextView tvTipo;
    TextView tvQuantidade;
    Bundle params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        params = intent.getExtras();

        tvMedicamento = (TextView)findViewById(R.id.tvMedicamento);
        tvNmPaciente = (TextView)findViewById(R.id.tvNmPaciente);
        tvDescricaoMedicamento = (TextView)findViewById(R.id.tvDescricaoMedicamento);
        tvFrequencia = (TextView)findViewById(R.id.tvFrequencia);
       // tvHorarios = (TextView)findViewById(R.id.tvHorarios);
        tvTipo = (TextView)findViewById(R.id.tvTipo);
        tvQuantidade = (TextView)findViewById(R.id.tvQuantidade);

        if (params != null) {
            int id = params.getInt("id");

            Alarme a = Query.one(Alarme.class, "select * from alarme where id=?", id).get();

            tvMedicamento.setText(a.getNomeMedicamento());
            tvNmPaciente.setText(a.getNomePaciente());
            tvDescricaoMedicamento.setText(a.getDescricaoMedicamento());
            tvFrequencia.setText(a.getIntervaloDe() + " em " + a.getIntervaloDe() + " horas");
           // tvHorarios.setText(a.getHorarios());
            tvTipo.setText(a.getDosagem());
            tvQuantidade.setText(a.getQuantidade());


        }






        imbStop = (ImageButton) findViewById(R.id.imbStop);



        imbStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (params != null) {
                    NotificationManager oldNoti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    int id = params.getInt("id");
                    oldNoti.cancel(id);

                }
                finish();
                Intent service = new Intent(getApplicationContext(), AlarmeService.class);
                stopService(service);


            }
        });


        if (savedInstanceState != null) {

        }

    }
}
