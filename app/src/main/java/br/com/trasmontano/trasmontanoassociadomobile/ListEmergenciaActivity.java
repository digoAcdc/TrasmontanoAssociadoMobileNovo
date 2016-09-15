package br.com.trasmontano.trasmontanoassociadomobile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.Emergencia;
import br.com.trasmontano.trasmontanoassociadomobile.adapter.AssociadoLoginAdapter;
import br.com.trasmontano.trasmontanoassociadomobile.adapter.EmergenciaAdapter;
import br.com.trasmontano.trasmontanoassociadomobile.network.APIClient;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListEmergenciaActivity extends AppCompatActivity {

    String matricula;
    String latitude;
    String longitude;
    Bundle params;
    private RecyclerView recyclerView;
    SpotsDialog spotsDialog;
    Callback<List<Emergencia>> callbackEmergencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_emergencia);

        recyclerView = (RecyclerView) findViewById(R.id.rvEmergencia);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        Intent intent = getIntent();
        params = intent.getExtras();

        if (params != null) {
            matricula = params.getString("matricula");
            latitude = params.getString("latitude");
            longitude = params.getString("longitude");

            configureEmergenciaCallback();

            new APIClient().getRestService().getHospitaisEmergenciaMobile(matricula,
                    latitude, longitude ,callbackEmergencia);
        }
    }


    private void configureEmergenciaCallback() {
        callbackEmergencia = new Callback<List<Emergencia>>() {

            @Override
            public void success(List<Emergencia> emergencias, Response response) {
                recyclerView.setAdapter(new EmergenciaAdapter(ListEmergenciaActivity.this, emergencias, onClickEmergencia()));
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ListEmergenciaActivity.this, "Falha ao conectar ao servidor", Toast.LENGTH_LONG).show();
            }
        };
    }

    private EmergenciaAdapter.EmergenciaOnClickListener onClickEmergencia() {
        return new EmergenciaAdapter.EmergenciaOnClickListener() {

            @Override
            public void OnClickButtonComoChegar(View view, int index) {
                Toast.makeText(ListEmergenciaActivity.this, "Como chegar", Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnClickButtonLigar(View view, int index) {

                TextView t = (TextView) view.findViewById(R.id.tvTelefone);
                String phone = t.getText().toString();

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(ListEmergenciaActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ListEmergenciaActivity.this, "O aplicativo não possui permissao para fazer ligaçoes, favor checar as permissões.", Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(callIntent);
            }
        };

    }
}
