package br.com.trasmontano.trasmontanoassociadomobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.AgendaMedicaAssociado;
import br.com.trasmontano.trasmontanoassociadomobile.adapter.AgendamentoConsultaAdapter;
import br.com.trasmontano.trasmontanoassociadomobile.network.APIClient;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListAgendamentoConsultaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageButton imbDeleteAgendamentoConsulta;
    SpotsDialog spotsDialog;
    private Callback<List<AgendaMedicaAssociado>> callbackAgendaMedicaAssociado;
    private ImageButton imAddConsultaMedica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_agendamento_consulta);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = (RecyclerView) findViewById(R.id.rvAgendamentoDeConsulta);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        spotsDialog = new SpotsDialog(this, R.style.LoaderCustom);
        spotsDialog.show();

        imAddConsultaMedica = (ImageButton)findViewById(R.id.imAddConsultaMedica);

        imAddConsultaMedica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(ListAgendamentoConsultaActivity.this, AgendamentoDeConsultaActivity.class);
                startActivity(i);
            }
        });

        configureInformacaoAgendaMedicaAssociadoCallback();

        SharedPreferences prefs = getSharedPreferences("DADOS_LOGIN", MODE_PRIVATE);
        String cdDependente = prefs.getString("CodigoDependente", "");
        String mat = prefs.getString("CodigoUsuario", "");

        new APIClient().getRestService().getAngendaMedicaAssociado(mat,
                cdDependente, 0, callbackAgendaMedicaAssociado);
    }


    private void configureInformacaoAgendaMedicaAssociadoCallback() {
        callbackAgendaMedicaAssociado = new Callback<List<AgendaMedicaAssociado>>() {
            @Override
            public void success(List<AgendaMedicaAssociado> agendaMedicaAssociados, Response response) {
                recyclerView.setAdapter(new AgendamentoConsultaAdapter(ListAgendamentoConsultaActivity.this, agendaMedicaAssociados, OnclickButtonDelete()));
                spotsDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                spotsDialog.dismiss();
                Toast.makeText(ListAgendamentoConsultaActivity.this, "Falha ao conectar ao servidor", Toast.LENGTH_LONG).show();
            }
        };
    }

    private AgendamentoConsultaAdapter.DeleteConsultaOnClickListener OnclickButtonDelete()
    {
        return  new AgendamentoConsultaAdapter.DeleteConsultaOnClickListener()
        {
            @Override
            public void OnClickImageButtonDelete(View view, int index) {
                Toast.makeText(ListAgendamentoConsultaActivity.this, "Deletar", Toast.LENGTH_LONG).show();
            }
        };
    }


}
