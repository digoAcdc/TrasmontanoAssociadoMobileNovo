package br.com.trasmontano.trasmontanoassociadomobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.Associado;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.Login;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.Preferencias;
import br.com.trasmontano.trasmontanoassociadomobile.network.APIClient;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.sprinkles.Query;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button btLogar;
    private Button btCarteirinhaSemLogin;
    private Button btAlarmeMedicamentos;
    SpotsDialog spotsDialog;
    private String redirecionarPara;


    private Callback<Login> callbackUsuario;

    CarouselView carouselView;

    int[] sampleImages = {R.drawable.agend, R.drawable.bannersantos, R.drawable.igesp, R.drawable.saude_integral};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        spotsDialog = new SpotsDialog(this, R.style.LoaderCustom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configureInformacaoAssociadoCallback();

        btLogar = (Button) findViewById(R.id.btLogar);
        btCarteirinhaSemLogin = (Button) findViewById(R.id.btCarteirinhaSemLogin);

        btCarteirinhaSemLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirecionarPara = "CarteirinhaTemporaria";
                showInputDialog();
                //CarteirinhaSemLogin();

            }
        });

        btAlarmeMedicamentos = (Button)findViewById(R.id.btAlarmeMedicamentos);

        btAlarmeMedicamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListAlarmeActivity.class);
                startActivity(i);
            }
        });


        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        btLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logar();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);

        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            Logar();
        } else if (id == R.id.nav_carteirinha_sem_logar) {
            redirecionarPara = "CarteirinhaTemporaria";
            showInputDialog();
            //CarteirinhaSemLogin();
        } else if (id == R.id.nav_home) {


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_alarme_medicamentos) {
            Intent i = new Intent(MainActivity.this, ListAlarmeActivity.class);
            startActivity(i);
        }

       /* } else if (id == R.id.nav_carteirinha_sem_logar) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Logar()
    {
        spotsDialog.show();
        List<Associado> lst = Query.all(Associado.class).get().asList();

        if(lst.size() > 0)
        {
            Intent i = new Intent(MainActivity.this, ListAssociadoActivity.class);
            startActivity(i);
            spotsDialog.dismiss();
        }
        else
        {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            spotsDialog.dismiss();
        }
    }
    public void CarteirinhaSemLogin()
    {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        Bundle params = new Bundle();

        params.putString("redirecionarPara", "CarteirinhaTemporaria");
        intent.putExtras(params);
        startActivity(intent);
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText etSenha = (EditText) promptView.findViewById(R.id.etSenha);
        final EditText etLogin = (EditText) promptView.findViewById(R.id.etLogin);


        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //resultText.setText("Hello, " + editText.getText());
                        if(etLogin.getText().toString().equals("") || etLogin.getText().toString().equals(""))
                        {
                            Toast.makeText(MainActivity.this, "Login/Senha Obrigatórios", Toast.LENGTH_LONG).show();
                        }
                        else {
                            spotsDialog.show();
                            new APIClient().getRestService().getLoginAssociado(etLogin.getText().toString(),
                                    etSenha.getText().toString(), callbackUsuario);
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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
                        Intent intent = new Intent(MainActivity.this, MainLogadoActivity.class);
                        startActivity(intent);
                    } else {
                        if (login.UsuarioValido == false) {
                            Toast.makeText(MainActivity.this, "Usuario Inválido", Toast.LENGTH_LONG).show();

                        } else if (login.SenhaValida == false) {
                            Toast.makeText(MainActivity.this, "Senha Inválida", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(MainActivity.this, "Login/Senha Inválidos", Toast.LENGTH_LONG).show();

                        }
                    }
                    spotsDialog.dismiss();
                } else {
                    if (login.UsuarioValido == false) {
                        Toast.makeText(MainActivity.this, "Usuario Inválido", Toast.LENGTH_LONG).show();

                    } else if (login.SenhaValida == false) {
                        Toast.makeText(MainActivity.this, "Senha Inválida", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(MainActivity.this, "Login/Senha Inválidos", Toast.LENGTH_LONG).show();

                    }
                }
                spotsDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERRO-------->", error.getMessage().toString());
                Toast.makeText(MainActivity.this, "Falha ao conectar no servidor", Toast.LENGTH_LONG).show();
                spotsDialog.dismiss();
            }
        };
    }

    public void GuardarDadosLoginAssociado(Login login, String redirecionarPara) {

        Preferencias p = new Preferencias(this);
        p.GuardarDadosLoginAssociado(login, redirecionarPara);

    }

}
