package br.com.trasmontano.trasmontanoassociadomobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.AgendaMedicaAssociado;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.Associado;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.Login;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.Preferencias;
import br.com.trasmontano.trasmontanoassociadomobile.network.APIClient;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.sprinkles.Query;

public  class LoginActivity extends AppCompatActivity  {
    private Button btEntrar;

    private Button btCadastrar;
    private TextInputLayout tiUsuario;
    private TextInputLayout tisenha;
    private String redirecionarPara;
    SpotsDialog spotsDialog;

    private Callback<Login> callbackUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Intent intent = getIntent();

        Bundle params = intent.getExtras();

        if (params != null) {
            redirecionarPara = params.getString("redirecionarPara");
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        spotsDialog = new SpotsDialog(this, R.style.LoaderCustom);

        configureInformacaoAssociadoCallback();

        btEntrar = (Button) findViewById(R.id.entrarLogin);
        btCadastrar = (Button) findViewById(R.id.btnCadastrarLogin);
        tiUsuario = (TextInputLayout) findViewById(R.id.tiUsuario);
        tisenha = (TextInputLayout) findViewById(R.id.tiSenha);

        SharedPreferences prefs = getSharedPreferences("MATRICULA_SELECIONADA_NA_LISTA", MODE_PRIVATE);
        if (prefs.contains("matricula")) {
            tiUsuario.getEditText().setText(prefs.getString("matricula", ""));
        }
        SharedPreferences.Editor editor = getSharedPreferences("MATRICULA_SELECIONADA_NA_LISTA", MODE_PRIVATE).edit();
        editor.remove("matricula");
        editor.apply();

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tiUsuario.getEditText().getText().toString().equals("") || tisenha.getEditText().getText().toString().equals("")) {
                    tisenha.setError("Usuario/Senha campos obrigatórios");
                    return;
                } else {
                    spotsDialog.show();
                    new APIClient().getRestService().getLoginAssociado(tiUsuario.getEditText().getText().toString(),
                            tisenha.getEditText().getText().toString(), callbackUsuario);
                }

            }
        });


        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spotsDialog.show();
                Intent i = new Intent(LoginActivity.this, CadastarActivity.class);
                startActivity(i);
                spotsDialog.dismiss();

            }
        });

        tisenha.requestFocus();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void configureInformacaoAssociadoCallback() {
        callbackUsuario = new Callback<Login>() {

            @Override
            public void success(Login login, Response response) {

                if (login.UsuarioValido == true & login.SenhaValida == true & login.getBloqueado() == 0) {
                    if (login.getSituacao().equalsIgnoreCase("A") | login.getSituacao().equalsIgnoreCase("S")) {

                        Associado a = Query.one(Associado.class, "select * from associado where usuario=?", login.getCodigoUsuario()).get();
                        if (a == null) {
                            a = new Associado();
                            a.setUsuario(login.getCodigoUsuario());
                            a.save();
                        }

                        GuardarDadosLoginAssociado(login, redirecionarPara);
                        finish();
                        Intent intent = new Intent(LoginActivity.this, MainLogadoActivity.class);
                        startActivity(intent);
                    } else {
                        if (login.UsuarioValido == false) {
                            Toast.makeText(LoginActivity.this, "Usuario Inválido", Toast.LENGTH_LONG).show();
                            tiUsuario.setError("Usuário Inválido");
                        } else if (login.SenhaValida == false) {
                            Toast.makeText(LoginActivity.this, "Senha Inválida", Toast.LENGTH_LONG).show();
                            tisenha.setError("Senha Inválida");
                        } else {
                            Toast.makeText(LoginActivity.this, "Login/Senha Inválidos", Toast.LENGTH_LONG).show();
                            tisenha.setError("Usuario/Senha Inválidos");
                        }
                    }
                    spotsDialog.dismiss();
                } else {
                    if (login.UsuarioValido == false) {
                        Toast.makeText(LoginActivity.this, "Usuario Inválido", Toast.LENGTH_LONG).show();
                        tiUsuario.setError("Usuário Inválido");
                    } else if (login.SenhaValida == false) {
                        Toast.makeText(LoginActivity.this, "Senha Inválida", Toast.LENGTH_LONG).show();
                        tisenha.setError("Senha Inválida");
                    } else {
                        Toast.makeText(LoginActivity.this, "Login/Senha Inválidos", Toast.LENGTH_LONG).show();
                        tisenha.setError("Usuario/Senha Inválidos");
                    }
                }
                spotsDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERRO-------->", error.getMessage().toString());
                Toast.makeText(LoginActivity.this, "Falha ao conectar no servidor", Toast.LENGTH_LONG).show();
                spotsDialog.dismiss();
            }
        };
    }

    public void GuardarDadosLoginAssociado(Login login, String redirecionarPara) {

        Preferencias p = new Preferencias(this);
        p.GuardarDadosLoginAssociado(login, redirecionarPara);
        /*SharedPreferences.Editor editor = getSharedPreferences("DADOS_LOGIN", MODE_PRIVATE).edit();
        editor.putString("Bloqueado", Integer.toString(login.getBloqueado()));
        editor.putString("ExpiraEm", Integer.toString(login.getExpiraEm()));
        editor.putString("StatusAcesso", Integer.toString(login.getStatusAcesso()));
        editor.putString("DataUltimoAcesso", login.getDataUltimoAcesso());
        editor.putString("CodigoUsuario", login.getCodigoUsuario());
        editor.putString("NomeUsuario", login.getNomeUsuario());
        editor.putString("Email", login.getEmail());
        editor.putString("PerfilUsuario", login.getPerfilUsuario());
        editor.putString("TipoPlano", login.getTipoPlano());

        if (redirecionarPara != "")
            editor.putString("redirecionarPara", redirecionarPara);

        editor.commit();*/
    }
}
