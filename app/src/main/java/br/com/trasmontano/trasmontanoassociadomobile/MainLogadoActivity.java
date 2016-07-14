package br.com.trasmontano.trasmontanoassociadomobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.antonionicolaspina.revealtextview.RevealTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.Associado;
import dmax.dialog.SpotsDialog;
import se.emilsjolander.sprinkles.Query;

public class MainLogadoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SpotsDialog spotsDialog;
    private Button btCarteirinha;
    private CircularImageView circularImageView;
    private RevealTextView revealTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        circularImageView = (CircularImageView) findViewById(R.id.ivAssociadoCard);
        spotsDialog = new SpotsDialog(this, R.style.LoaderCustom);
        SharedPreferences prefs = getSharedPreferences("DADOS_LOGIN", MODE_PRIVATE);
        String nome = prefs.getString("NomeUsuario", "");
        String mat = prefs.getString("CodigoUsuario", "");
        String redirecionarPara = prefs.getString("redirecionarPara", "");

        Associado a = Query.one(Associado.class, "select * from associado where usuario=?", mat).get();

        if (a.getCaminhoImagem() != null) {
            File file = new File(a.getCaminhoImagem());
            if (file.exists()) {
                circularImageView.setImageURI(Uri.parse(a.getCaminhoImagem()));
            }
        }

        btCarteirinha = (Button) findViewById(R.id.btCarteirinha);

        btCarteirinha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainLogadoActivity.this, CarteirinhaActivity.class);
                startActivity(i);
            }
        });

        revealTextView = (RevealTextView) findViewById(R.id.rtvLabel);

        revealTextView.setAnimatedText("Bem vindo, " + nome + "!");

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
            Intent i = new Intent(this, CarteirinhaActivity.class);
            startActivity(i);
        }

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
}
