package br.com.trasmontano.trasmontanoassociadomobile;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.Associado;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.EmailCanalAtendimento;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.Login;
import br.com.trasmontano.trasmontanoassociadomobile.network.APIClient;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.sprinkles.Query;


public class EmailActivity extends AppCompatActivity {

    static String tipo;
    static String email;
    SpotsDialog spotsDialog;
    TextView tvSetor;
    TextView etNomeEmail;
    TextView etMatriculaEmail;
    TextView etCdDependenteEmail;
    TextView etEmail;
    TextView etAssuntoEmail;
    TextView etMensagemEmail;
    ImageButton imbEnviarEmail;
    ImageButton imbLimpar;

    private Callback<String> callbackEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        spotsDialog = new SpotsDialog(this, R.style.LoaderCustom);
        tvSetor = (TextView) findViewById(R.id.tvSetor);
        etNomeEmail = (TextView) findViewById(R.id.etNomeEmail);
        etMatriculaEmail = (TextView) findViewById(R.id.etMatriculaEmail);
        etCdDependenteEmail = (TextView) findViewById(R.id.etCdDependenteEmail);
        etEmail = (TextView) findViewById(R.id.etEmail);
        etAssuntoEmail = (TextView) findViewById(R.id.etAssuntoEmail);
        etMensagemEmail = (TextView) findViewById(R.id.etMensagemEmail);
        imbEnviarEmail = (ImageButton) findViewById(R.id.imbEnviarEmail);
        imbLimpar = (ImageButton) findViewById(R.id.imbLimpar);

        Intent intent = getIntent();

        Bundle params = intent.getExtras();

        if (params != null) {
            tipo = params.getString("tipo");
            email = params.getString("tipo");
        }

        tvSetor.setText("Setor: " + tipo);


       /* imbEnviarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

       /* imbLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNomeEmail.setText("");
                etMatriculaEmail.setText("");
                etCdDependenteEmail.setText("");
                etEmail.setText("");
                etAssuntoEmail.setText("");
                etMensagemEmail.setText("");
            }
        });*/

        configureEmailCallback();
        EmailCanalAtendimento e = new EmailCanalAtendimento();
        e.setAssunto("assunto");
        e.setCdDependente("00");
        e.setEmail("fsafas");
        e.setMatricula("55400");
        e.setMensagem("bla");
        e.setNome("Rodrigo");

        new APIClient().getRestService().enviaEmail(e, callbackEmail);

    }

    private void configureEmailCallback() {
        callbackEmail = new Callback<String>() {


            @Override
            public void success(String s, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        };
    }
}

