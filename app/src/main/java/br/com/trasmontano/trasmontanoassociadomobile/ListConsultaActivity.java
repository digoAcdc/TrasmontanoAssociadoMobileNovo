package br.com.trasmontano.trasmontanoassociadomobile;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.Associado;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.DadosConsulta;
import br.com.trasmontano.trasmontanoassociadomobile.adapter.AssociadoLoginAdapter;
import br.com.trasmontano.trasmontanoassociadomobile.adapter.ConsultaAdapter;
import dmax.dialog.SpotsDialog;
import se.emilsjolander.sprinkles.Query;

public class ListConsultaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    SpotsDialog spotsDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_consulta);
        spotsDialog = new SpotsDialog(this, R.style.LoaderCustom);
        spotsDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.rvConsulta);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



        List<DadosConsulta> lst = AgendamentoDeConsultaActivity.lstDadosConsulta;

        CarregaLista(this, lst );

    }

    public void CarregaLista(Context context, List<DadosConsulta> lst) {
        spotsDialog.show();
        recyclerView.setAdapter(new ConsultaAdapter(this, lst, onClickAgendar()));
        spotsDialog.dismiss();
    }

    private ConsultaAdapter.AgendarConsultaOnClickListener onClickAgendar(){
        return new ConsultaAdapter.AgendarConsultaOnClickListener()
        {
            @Override
            public void OnClickImageButtonAgendar(View view, int index) {
                Toast.makeText(ListConsultaActivity.this, "Agendar", Toast.LENGTH_LONG).show();
            }
        };
    }
}
