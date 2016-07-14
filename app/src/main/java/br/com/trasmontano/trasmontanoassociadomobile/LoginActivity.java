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

import br.com.trasmontano.trasmontanoassociadomobile.DTO.Associado;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.Login;
import br.com.trasmontano.trasmontanoassociadomobile.network.APIClient;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.sprinkles.Query;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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

        if(params!=null)
        {
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
                //Toast.makeText(MainActivity.this, "cadastrar", Toast.LENGTH_LONG).show();
            }
        });

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void configureInformacaoAssociadoCallback() {
        callbackUsuario = new Callback<Login>() {

            @Override
            public void success(Login login, Response response) {

                if (login.UsuarioValido == true & login.SenhaValida == true & login.getBloqueado() == 0) {
                    if (login.getSituacao() != "A" & login.getSituacao() != "S") {

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
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Usuario invalido", Toast.LENGTH_LONG).show();
                    }
                    spotsDialog.dismiss();
                } else {
                    Toast.makeText(LoginActivity.this, "Usuario invalido", Toast.LENGTH_LONG).show();
                }
                spotsDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERRO-------->", error.getMessage().toString());
                Toast.makeText(LoginActivity.this, "Falha ao conectar no servidor", Toast.LENGTH_LONG).show();
                spotsDialog.dismiss();
                //spotsDialog.dismiss();
            }
        };
    }

    public void GuardarDadosLoginAssociado(Login login, String redirecionarPara) {
        SharedPreferences.Editor editor = getSharedPreferences("DADOS_LOGIN", MODE_PRIVATE).edit();
        editor.putString("Bloqueado", Integer.toString(login.getBloqueado()));
        editor.putString("ExpiraEm", Integer.toString(login.getExpiraEm()));
        editor.putString("StatusAcesso", Integer.toString(login.getStatusAcesso()));
        editor.putString("DataUltimoAcesso", login.getDataUltimoAcesso());
        editor.putString("CodigoUsuario", login.getCodigoUsuario());
        editor.putString("NomeUsuario", login.getNomeUsuario());
        editor.putString("Email", login.getEmail());
        editor.putString("PerfilUsuario", login.getPerfilUsuario());
        editor.putString("TipoPlano", login.getTipoPlano());

        if(redirecionarPara != "")
            editor.putString("redirecionarPara", redirecionarPara);

        editor.commit();
    }
}
