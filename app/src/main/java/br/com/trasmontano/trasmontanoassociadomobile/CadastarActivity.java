package br.com.trasmontano.trasmontanoassociadomobile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.Associado;
import br.com.trasmontano.trasmontanoassociadomobile.DTO.DadosConsulta;
import livroandroid.lib.utils.ImageResizeUtils;
import livroandroid.lib.utils.SDCardUtils;
import retrofit.Callback;

public class CadastarActivity extends AppCompatActivity {
    private Button btCadastrar;
    private TextInputLayout tiMatricula;
    private TextInputLayout tiCPF;
    private TextInputLayout tiDataNascimento;
    private TextInputLayout tiEmail;
    private TextInputLayout tiConfirmaEmail;
    private TextInputLayout tiSenha;
    private TextInputLayout tiRepetirSenha;
    private TextInputLayout tiLembrarSenha;
    private CircularImageView imageView;
    private File file;
    private ImageButton imbData;
    private Calendar cal;
    private int day;
    private int month;
    private int year;

    private Callback<String> callbackUsuarioExiste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageView = (CircularImageView) findViewById(R.id.imagem);
        btCadastrar = (Button) findViewById(R.id.btCadastrarAssociado);

        tiMatricula = (TextInputLayout) findViewById(R.id.tiMatricula);

        tiCPF = (TextInputLayout) findViewById(R.id.tiCPF);
        tiDataNascimento = (TextInputLayout) findViewById(R.id.tiDataNascimento);
        tiEmail = (TextInputLayout) findViewById(R.id.tiEmail);
        tiConfirmaEmail = (TextInputLayout) findViewById(R.id.tiConfirmaEmail);
        tiSenha = (TextInputLayout) findViewById(R.id.tiSenha);
        tiRepetirSenha = (TextInputLayout) findViewById(R.id.tiRepetirSenha);
        tiLembrarSenha = (TextInputLayout) findViewById(R.id.tiLembrarSenha);

        ImageButton b = (ImageButton) findViewById(R.id.btAbrirCamera);

        imbData = (ImageButton) findViewById(R.id.imbData);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        imbData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });


        b.setOnClickListener(new View.OnClickListener() {
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

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (VaidarDados()) {
                    Associado a = new Associado();
                    a.setUsuario(tiMatricula.getEditText().getText().toString());
                    a.setCpf(tiCPF.getEditText().getText().toString());
                    a.setDataNascimento(tiDataNascimento.getEditText().getText().toString());
                    a.setEmail(tiEmail.getEditText().getText().toString());
                    a.setLembreteSenha(tiLembrarSenha.getEditText().getText().toString());

                    if (file != null && file.exists()) {
                        a.setCaminhoImagem(file.toString());
                    }
                    try {

                        if (a.save()) {
                            Intent i = new Intent(CadastarActivity.this, ListAssociadoActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(CadastarActivity.this, "Falha ao cadastrar", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex) {
                        Log.d("", ex.getMessage().toString());

                    }
                }
            }
        });

        if (savedInstanceState != null) {
            // Se girou a tela recupera o estado
            file = (File) savedInstanceState.getSerializable("file");
            showImage(file);
        }

        imageView.requestFocus();

    }

    public boolean VaidarDados() {
        if (tiDataNascimento.getEditText().getText().equals("")) {
            tiDataNascimento.setError("Campo data nascimento obrigatório");
            return false;
        }
        if (tiEmail.getEditText().getText().equals("")) {
            tiEmail.setError("Campo email obrigatório");
            return false;
        }
        if (tiConfirmaEmail.getEditText().getText().equals("")) {
            tiConfirmaEmail.setError("Campo repetir email obrigatório");
            return false;
        }
        if (tiEmail.getEditText().getText() != tiConfirmaEmail.getEditText().getText()) {
            tiConfirmaEmail.setError("Campo email divergente do anterior");
            return false;
        }
        if (tiSenha.getEditText().getText().equals("")) {
            tiSenha.setError("Campo senha obrigatório");
            return false;
        }
        if (tiRepetirSenha.getEditText().getText().equals("")) {
            tiRepetirSenha.setError("Campo repetir senha obrigatório");
            return false;
        }
        if (tiSenha.getEditText().getText() != tiRepetirSenha.getEditText().getText()) {
            tiRepetirSenha.setError("Campo repetir senha divergente do anterior");
            return false;
        }
        if (tiLembrarSenha.getEditText().getText().equals("")) {
            tiLembrarSenha.setError("Campo lembrar senha obrigatório");
            return false;
        }

        return true;
    }

    private void showImage(File file) {
        if (file != null && file.exists()) {
            Log.d("foto", file.getAbsolutePath());

            int w = imageView.getWidth();
            int h = imageView.getHeight();
            Bitmap bitmap = ImageResizeUtils.getResizedImage(Uri.fromFile(file), w, h, false);
            // Toast.makeText(this, "w/h:" + imgView.getWidth() + "/" + imgView.getHeight() + " > " + "w/h:" + bitmap.getWidth() + "/" + bitmap.getHeight(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "file:" + file, Toast.LENGTH_LONG).show();

            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && file != null) {
            showImage(file);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Salvar o estado caso gire a tela
        outState.putSerializable("file", file);
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            tiDataNascimento.getEditText().setText(selectedDay + "/" + (selectedMonth + 1) + "/"
                    + selectedYear);
        }
    };
}
