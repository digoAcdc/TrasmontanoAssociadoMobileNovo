package br.com.trasmontano.trasmontanoassociadomobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.DadosCarteirinha;
import br.com.trasmontano.trasmontanoassociadomobile.network.APIClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CarteirinhaEmpresarialFrenteFragment extends Fragment {

    private Callback<List<DadosCarteirinha>> callbackCarteirinhaTemporaria;
    private TextView tvNome;
    private TextView tvInicioVigencia;
    private TextView tvCns;
    private TextView tvDtNascimento;
    private TextView tvPlano;
    private TextView tvEmpresa;
    private TextView tvMatricula;


    private static String Matricula;
    private static String Dependente;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_carteirinha_empresarial_frente, container, false);

        tvNome = (TextView)view.findViewById(R.id.tvNomeCarteirinha);
        tvInicioVigencia = (TextView)view.findViewById(R.id.tvInicioVigencia);
        tvCns = (TextView)view.findViewById(R.id.tvCns);
        tvDtNascimento = (TextView)view.findViewById(R.id.tvDtNascimento);
        tvPlano = (TextView)view.findViewById(R.id.tvPlano);
        tvEmpresa = (TextView)view.findViewById(R.id.tvEmpresa);
        tvMatricula = (TextView)view.findViewById(R.id.tvMatricula);


        SharedPreferences prefs = this.getActivity().getSharedPreferences("DADOS_LOGIN", Context.MODE_PRIVATE);
        if (prefs.contains("CodigoUsuario")) {
            Matricula =  prefs.getString("CodigoUsuario", "");

            if(Matricula.length() > 6)
            {
                Dependente = Matricula.substring(5,2);
            }
            else
            {
                Dependente = "00";
            }
        }

        configureInformacaoAssociadoCallback();

        new APIClient().getRestService().getDadosCarteirinhaTemporaria(Matricula,
                Dependente, callbackCarteirinhaTemporaria);

        return view;
    }
    private void configureInformacaoAssociadoCallback() {
        callbackCarteirinhaTemporaria = new Callback<List<DadosCarteirinha>>() {

            @Override
            public void success(List<DadosCarteirinha> dadosCarteirinha, Response response) {
                DadosCarteirinha c = dadosCarteirinha.get(0);

                String dtNasc = c.getDtNasc().substring(0, 10);
                String[] nascParts = dtNasc.split("-");

                String iniVigencia = c.getAdmissao().substring(0, 10);
                String[] vigenciaParts = iniVigencia.split("-");

                tvNome.setText("Tit.: " + c.getNome());
                tvInicioVigencia.setText(vigenciaParts[2] + "/" + vigenciaParts[1] + "/" + vigenciaParts[0]);
                tvCns.setText(c.getCNS());
                tvDtNascimento.setText(nascParts[2] + "/" + nascParts[1] + "/" + nascParts[0]);
                tvPlano.setText(c.getDsCategoria() + "/Reg. " + c.getNumRegAns());
                tvEmpresa.setText(c.getRazaoSocial());
                tvMatricula.setText(Matricula + "." + Dependente);
            }

            public void failure(RetrofitError error) {
                Log.d("ERRO-------->", error.getMessage().toString());
                //Toast.makeText(LoginActivity.this, "Falha ao conectar no servidor", Toast.LENGTH_LONG).show();
                //spotsDialog.dismiss();
                //spotsDialog.dismiss();
            }

           /* @Override
            public void success(Login login, Response response) {

                if (login.UsuarioValido == true & login.SenhaValida == true & login.getBloqueado() == 0) {
                    if (login.getSituacao() != "A" & login.getSituacao() != "S") {

                        Associado a = Query.one(Associado.class, "select * from associado where usuario=?", login.getCodigoUsuario()).get();
                        if (a == null) {
                            a = new Associado();
                            a.setUsuario(login.getCodigoUsuario());
                            a.save();
                        }
                        GuardarDadosLoginAssociado(login);
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
                spotsDialog.dismiss();*/

        };
    }
}
