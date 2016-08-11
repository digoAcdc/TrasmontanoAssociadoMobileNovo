package br.com.trasmontano.trasmontanoassociadomobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.antonionicolaspina.revealtextview.RevealTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.AgendaMedicaAssociado;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.Associado;
import br.com.trasmontano.trasmontanoassociadomobile.network.APIClient;
import dmax.dialog.SpotsDialog;
import livroandroid.lib.utils.ImageResizeUtils;
import livroandroid.lib.utils.SDCardUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.sprinkles.Query;

public class MainLogadoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SpotsDialog spotsDialog;
    private Button btCarteirinha;
    private Button btAgendamentoConsulta;
    private CircularImageView circularImageView;
    private RevealTextView revealTextView;
    private RevealTextView revealTextViewNome;
    private String TipoPlano;
    private TextView tvQtdConsultas;
    private Callback<List<AgendaMedicaAssociado>> callbackAgendaMedicaAssociado;
    private Button btnQtdConsultas;
    private File file;
    String mat;
    String cdDependente;
    Associado a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        circularImageView = (CircularImageView) findViewById(R.id.ivAssociadoCard);
        spotsDialog = new SpotsDialog(this, R.style.LoaderCustom);
        btnQtdConsultas = (Button)findViewById(R.id.btnQtdConsultas);
        SharedPreferences prefs = getSharedPreferences("DADOS_LOGIN", MODE_PRIVATE);
         String nome = prefs.getString("NomeUsuario", "");
         mat = prefs.getString("CodigoUsuario", "");
         cdDependente = prefs.getString("CodigoDependente", "");
        String redirecionarPara = prefs.getString("redirecionarPara", "");
        TipoPlano = prefs.getString("PerfilUsuario", "");
        prefs.edit().remove("redirecionarPara").commit();

        configureInformacaoAgendaMedicaAssociadoCallback();
        Associado a = null;

        if(cdDependente.equalsIgnoreCase("00"))
        {
             a = Query.one(Associado.class, "select * from associado where usuario=?", mat).get();
        }
        else
        {
             a = Query.one(Associado.class, "select * from associado where usuario=?", mat + cdDependente).get();
        }

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                //get current date time with Date()
                Date date = new Date();
                //System.out.println(dateFormat.format(date));

                file = SDCardUtils.getPrivateFile(getBaseContext(), dateFormat.format(date) + ".jpg", Environment.DIRECTORY_PICTURES);
                // Chama a intent informando o arquivo para salvar a foto
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(i, 0);
            }
        });

        if (a != null) {
            if(a.getCaminhoImagem() != null) {
                File file = new File(a.getCaminhoImagem());
                if (file.exists()) {
                    circularImageView.setImageURI(Uri.parse(a.getCaminhoImagem()));
                }
            }
        }

        btCarteirinha = (Button) findViewById(R.id.btCarteirinha);

        btCarteirinha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarteirinhaVirtual();
            }
        });

        btAgendamentoConsulta = (Button)findViewById(R.id.btagendamentoDeConsulta);

        btAgendamentoConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainLogadoActivity.this, ListAgendamentoConsultaActivity.class);
                startActivity(i);
            }
        });

        btnQtdConsultas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btnQtdConsultas.getText().equals("") || !btnQtdConsultas.getText().equals("0 Consulta(s) Agendada(s)")) {
                    Intent i = new Intent(MainLogadoActivity.this, ListAgendamentoConsultaActivity.class);
                    startActivity(i);
                }
            }
        });

        revealTextView = (RevealTextView) findViewById(R.id.rtvLabelNome);
        revealTextViewNome = (RevealTextView) findViewById(R.id.rtvLabelMatricula);

        revealTextView.setAnimatedText("Bem vindo, " + nome + "!");
        revealTextViewNome.setAnimatedText("Matrícula, " + mat + "");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (redirecionarPara.equalsIgnoreCase("CarteirinhaTemporaria")) {
            CarteirinhaVirtual();
        }

        new APIClient().getRestService().getAngendaMedicaAssociado(mat,
                cdDependente, 0, callbackAgendaMedicaAssociado);

    }

    public void Encerrar() {
        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && file != null) {

            SharedPreferences prefs = getSharedPreferences("DADOS_LOGIN", MODE_PRIVATE);

            mat = prefs.getString("CodigoUsuario", "");
            cdDependente = prefs.getString("CodigoDependente", "");
            if(cdDependente.equalsIgnoreCase("00"))
            {
                a = Query.one(Associado.class, "select * from associado where usuario=?", mat).get();
            }
            else
            {
                a = Query.one(Associado.class, "select * from associado where usuario=?", mat + cdDependente).get();
            }

            if (file != null && file.exists()) {
                a.setCaminhoImagem(file.toString());

                a.save();
                circularImageView.setImageURI(Uri.parse(a.getCaminhoImagem()));
            }
            //showImage(file);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Salvar o estado caso gire a tela
        outState.putSerializable("file", file);
    }

    public void CarteirinhaVirtual() {
        Intent i = new Intent(this, CarteirinhaActivity.class);
        startActivity(i);
    }

    private void configureInformacaoAgendaMedicaAssociadoCallback() {
        callbackAgendaMedicaAssociado = new Callback<List<AgendaMedicaAssociado>>() {

            @Override
            public void success(List<AgendaMedicaAssociado> agendaMedicaAssociados, Response response) {

              btnQtdConsultas.setText(String.valueOf(agendaMedicaAssociados.size()) + " Consulta(s) Agendada(s)");


            }

            @Override
            public void failure(RetrofitError error) {

            }
        };

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();

            OpcaoEncerrarApp();
           /* DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Encerrar();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Deseja encerrar o aplicativo?").setPositiveButton("Sim", dialogClickListener)
                    .setNegativeButton("Não", dialogClickListener).show();*/
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_logado, menu);
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

        if (id == R.id.nav_carteirinha_virtual) {
            CarteirinhaVirtual();
        } else if (id == R.id.nav_encerrar) {
            OpcaoEncerrarApp();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_alarme_medicamentos) {
            Intent i = new Intent(MainLogadoActivity.this, ListAlarmeActivity.class);
            startActivity(i);

        }/* else if (id == R.id.nav_share) {

        }else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void OpcaoEncerrarApp() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Encerrar();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja encerrar o aplicativo?").setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }
}
