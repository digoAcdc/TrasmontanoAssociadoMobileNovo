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

import java.util.Calendar;
import java.util.List;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.Alarme;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.Associado;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.LogMedicamentosTomados;
import br.com.trasmontano.trasmontanoassociadomobile.Util.AlarmUtil;
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
    ImageButton imbTomeiMedicamento;

    ImageButton imbMaisDez;
    LogMedicamentosTomados log;
    int hours = 0;
    int minutes = 0;
    int day = 0;
    int month = 0;
    int year = 0;


    String hora = "";
    String minuto = "";
    String dia = "";
    String mes = "";
    String ano = "";
    int id = 0;
    Alarme a = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        params = intent.getExtras();

        /*Calendar c = Calendar.getInstance();

         hours = c.get(c.HOUR_OF_DAY);
         minutes = c.get(c.MINUTE);
         day = c.get(c.DAY_OF_MONTH);
         month = c.get(c.MONTH);
         year = c.get(c.YEAR);


         hora = String.valueOf(hours);
         minuto = String.valueOf(minutes);
         dia = String.valueOf(day);
         mes = String.valueOf(month + 1);
         ano = String.valueOf(year);*/

        CarregaData();

        tvMedicamento = (TextView) findViewById(R.id.tvMedicamento);
        tvNmPaciente = (TextView) findViewById(R.id.tvNmPaciente);
        tvDescricaoMedicamento = (TextView) findViewById(R.id.tvDescricaoMedicamento);
        tvFrequencia = (TextView) findViewById(R.id.tvFrequencia);
        // tvHorarios = (TextView)findViewById(R.id.tvHorarios);
        tvTipo = (TextView) findViewById(R.id.tvTipo);
        tvQuantidade = (TextView) findViewById(R.id.tvQuantidade);
        imbMaisDez = (ImageButton) findViewById(R.id.imbMaisDesz);
        imbTomeiMedicamento = (ImageButton) findViewById(R.id.imbTomeiMedicamento);

        if (params != null) {
            id = params.getInt("id");

            a = Query.one(Alarme.class, "select * from alarme where id=?", id).get();


            /*log = new LogMedicamentosTomados();
            log.setIdAlarme(id);
            log.setDataTomouMedicamento("00".substring(dia.length()) + dia + "/" + "00".substring(mes.length()) + mes + "/" + "0000".substring(ano.length()) + ano);
            log.setHoraTomouMedicamento(hora + ":" + minuto);
            log.setTomei(0);
            log.setNomeMedicamento(a.getNomeMedicamento());
            log.setUsuario(a.getNomePaciente());
            log.save();*/


            tvMedicamento.setText(a.getNomeMedicamento());
            tvNmPaciente.setText(a.getNomePaciente());
            tvDescricaoMedicamento.setText(a.getDescricaoMedicamento());
            tvFrequencia.setText(a.getIntervaloDe() + " em " + a.getIntervaloDe() + " horas");
            // tvHorarios.setText(a.getHorarios());
            tvTipo.setText(a.getDosagem());
            tvQuantidade.setText(a.getQuantidade());

            imbStop = (ImageButton) findViewById(R.id.imbStop);


            imbStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = 0;
                    if (params != null) {
                        NotificationManager oldNoti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        id = params.getInt("id");
                        oldNoti.cancel(id);

                    }
                    InsereLogAlarme(0, false);

                    Intent service = new Intent(getApplicationContext(), AlarmeService.class);
                    stopService(service);

                    Intent i = new Intent(MensagemActivity.this, ListLogAlarmeActivity.class);
                    i.putExtra("id", id);
                    startActivity(i);

                    finish();


                }
            });

            imbMaisDez.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = 0;
                    if (params != null) {
                        id = params.getInt("id");
                    }
                    List<LogMedicamentosTomados> lst = Query.all(LogMedicamentosTomados.class).get().asList();


                    NotificationManager oldNoti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    oldNoti.cancel(id);

                    NotificationManager oldNoti1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    oldNoti1.cancel(-1);
                    InsereLogAlarme(0, true);
                    // a = Query.one(Alarme.class, "select * from alarme where id=?", id).get();
                    Intent intent = new Intent(AlarmeBroadcastReceiver.ACTION);
                    intent.putExtra("id", id);
                    intent.putExtra("paciente", a.getNomePaciente());
                    intent.putExtra("medicamento", a.getNomeMedicamento());
                    intent.putExtra("mp3", a.getNomeMusica());
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.MINUTE, 10);
                    AlarmUtil.schedule(MensagemActivity.this, intent, c.getTimeInMillis(), -1);

                    Intent service = new Intent(getApplicationContext(), AlarmeService.class);
                    stopService(service);

                    Intent i = new Intent(MensagemActivity.this, ListLogAlarmeActivity.class);
                    i.putExtra("id", id);
                    startActivity(i);

                    /*log = Query.one(LogMedicamentosTomados.class, "select * from logMedicamentosTomados where idAlarme=?", id).get();
                    if(log == null)
                    {
                        log.setIdAlarme(id);
                        log.setUsuario(a.getNomePaciente());
                        log.setNomeMedicamento(a.getNomeMedicamento());
                        log.setDataTomouMedicamento("");
                        log.setHoraTomouMedicamento("");
                        log.save();
                    }*/

                    finish();
                }
            });

            imbTomeiMedicamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = 0;
                    if (params != null) {
                        id = params.getInt("id");
                    }
                    CarregaData();
                    NotificationManager oldNoti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    oldNoti.cancel(id);

                    NotificationManager oldNoti1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    oldNoti1.cancel(-1);

                   /* log = Query.one(LogMedicamentosTomados.class, "select * from logMedicamentosTomados where idAlarme=?", id).get();

                    log.setDataTomouMedicamento("00".substring(dia.length()) + dia + "/" + "00".substring(mes.length()) + mes + "/" + "0000".substring(ano.length()) + ano);
                    log.setHoraTomouMedicamento(hora + ":" + minuto);

                    log.save();*/
                    if (!InsereLogAlarme(1, false)) {
                        Toast.makeText(MensagemActivity.this, "", Toast.LENGTH_LONG).show();
                    }

                    Intent service = new Intent(getApplicationContext(), AlarmeService.class);
                    stopService(service);


                    Intent i = new Intent(MensagemActivity.this, ListLogAlarmeActivity.class);
                    i.putExtra("id", id);
                    startActivity(i);

                    finish();
                }
            });
        }


        if (savedInstanceState != null) {

        }

    }

    private boolean InsereLogAlarme(int tomei, boolean mais10Min) {
        CarregaData();
        log = new LogMedicamentosTomados();
        log.setIdAlarme(id);
        log.setDataTomouMedicamento("00".substring(dia.length()) + dia + "/" + "00".substring(mes.length()) + mes + "/" + "0000".substring(ano.length()) + ano);
        if (mais10Min)
            log.setHoraTomouMedicamento("+ 10 min");
        else
            log.setHoraTomouMedicamento(hora + ":" + minuto);
        log.setTomei(tomei);
        log.setNomeMedicamento(a.getNomeMedicamento());
        log.setUsuario(a.getNomePaciente());

        return log.save();


    }

    private void CarregaData() {
        Calendar c = Calendar.getInstance();

        hours = c.get(c.HOUR_OF_DAY);
        minutes = c.get(c.MINUTE);
        day = c.get(c.DAY_OF_MONTH);
        month = c.get(c.MONTH);
        year = c.get(c.YEAR);


        hora = String.valueOf(hours);
        minuto = String.valueOf(minutes);
        dia = String.valueOf(day);
        mes = String.valueOf(month + 1);
        ano = String.valueOf(year);
    }
}
