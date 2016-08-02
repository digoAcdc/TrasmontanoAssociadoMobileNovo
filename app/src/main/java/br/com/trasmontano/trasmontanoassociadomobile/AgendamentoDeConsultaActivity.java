package br.com.trasmontano.trasmontanoassociadomobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.List;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.AgendaMedicaAssociado;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.AgendamentoMedicoWebParametros;
import br.com.trasmontano.trasmontanoassociadomobile.network.APIClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AgendamentoDeConsultaActivity extends AppCompatActivity {
    MaterialSpinner spnEspecialidade;
    private Callback<List<AgendamentoMedicoWebParametros>> callbackAgendamentoMedicoWebParametros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento_de_consulta);

        spnEspecialidade = (MaterialSpinner)findViewById(R.id.spnEspecialidades);

        configureInformacaoAgendamentoMedicoWebParametrosCallback();

        new APIClient().getRestService().getAgendamentoMedicoWebParametros((byte) 0,
                0, 0, callbackAgendamentoMedicoWebParametros);



        spnEspecialidade.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Toast.makeText(AgendamentoDeConsultaActivity.this, "fsafsa", Toast.LENGTH_LONG);
            }
        });
    }

    private void configureInformacaoAgendamentoMedicoWebParametrosCallback() {
        callbackAgendamentoMedicoWebParametros = new Callback<List<AgendamentoMedicoWebParametros>>() {
            @Override
            public void success(List<AgendamentoMedicoWebParametros> agendamentoMedicoWebParametroses, Response response) {
                spnEspecialidade.setItems(agendamentoMedicoWebParametroses);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        };
    }
}
