package br.com.trasmontano.trasmontanoassociadomobile.network;

import java.util.List;

import br.com.trasmontano.trasmontanoassociadomobile.DTO.*;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by rbarbosa on 11/07/2016.
 */
public class APIClient {

    private static RestAdapter REST_ADAPTER;

    private static void createAdapterIfNeeded() {
        if (REST_ADAPTER == null) {
            REST_ADAPTER = new RestAdapter.Builder()
                    .setEndpoint("http://webapi.trasmontano.com.br")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient())
                    .build();
        }
    }

    public APIClient() {
        createAdapterIfNeeded();
    }

    public RestServices getRestService() {
        return REST_ADAPTER.create(RestServices.class);
    }

    public interface RestServices {
        @GET("/acesso/autenticacaomobile")
        void getLoginAssociado(
                @Header("usuario") String usuario,
                @Header("senha") String senha,
                Callback<Login> callbackUsuario
        );
        @GET("/acesso/autenticacaomobilebiometria")
        void getLoginAssociadoBiometria(
                @Header("usuario") String usuario,
                @Header("telefone") String telefone,
                Callback<Login> callbackUsuario
        );


        @GET("/associado/carencia/{matricula}/{dependente}")
        void getDadosCarteirinhaTemporaria(
                @Path("matricula") String matricula ,
                @Path("dependente") String dependente,
                Callback<List<DadosCarteirinha>> callbackCarteirinhaTemporaria
        );
        @GET("/associado/agendaMedicaAssociado/{matricula}/{dependente}/{tipo}")
        void getAngendaMedicaAssociado(
                @Path("matricula") String matricula ,
                @Path("dependente") String dependente,
                @Path("tipo") int tipo,
                Callback<List<AgendaMedicaAssociado>> callbackAgendaMedicaAssociado
        );
        @GET("/associado/agendamentoMedicoWebParametros/{tipo}/{cdEspecialidade}/{cdLocalidade}")
        void getAgendamentoMedicoWebParametros(
                @Path("tipo") byte tipo ,
                @Path("cdEspecialidade") int cdEspecialidade,
                @Path("cdLocalidade") int cdLocalidade,
                Callback<List<AgendamentoMedicoWebParametros>> callbackAgendamentoMedicoWebParametros
        );

        @GET("/associado/agendamentoMedicoWebDadosAssociado/{matricula}/{cdDependente}")
        void getAgendamentoMedicoWebDadosAssociado(
                @Path("matricula") String matricula ,
                @Path("cdDependente") String cdDependente,
                Callback<Associado> callbackAgendamentoMedicoWebDadosAssociado
        );
        @GET("/associado/agendamentoMedicoWeb/{Inicio}/{Fim}/{cdEspecialidade}/{cdLocalidade}/{cdMedico}/{sexo}/{idade}/{tipo}/{cdReferencia}")
        void getAgendamentoMedicoWeb(
                @Path("Inicio") String Inicio ,
                @Path("Fim") String Fim,
                @Path("cdEspecialidade") int cdEspecialidade,
                @Path("cdLocalidade") int cdLocalidade,
                @Path("cdMedico") int cdMedico,
                @Path("sexo") String sexo,
                @Path("idade") int idade,
                @Path("tipo") boolean tipo,
                @Path("cdReferencia") int cdReferencia,
                Callback<List<DadosConsulta>> callbackAgendamentoMedicoWeb
        );
       /* @GET("/associado/cancelamentoDeConsulta/{matricula}/{cdDependente}/{id}/{usuRede}/{usuSistema}/{tipo}/{solicitante}/{situacao}/{assunto}")
        void setCancelamentoDeConsulta(
                @Header("matricula") String matricula ,
                @Header("cdDependente") String cdDependente,
                @Header("id") int id,
                @Header("usuRede") String usuRede,
                @Header("usuSistema") String usuSistema,
                @Header("tipo") char tipo,
                @Header("solicitante") String solicitante,
                @Header("situacao") String situacao,
                @Header("assunto") String assunto,
                Callback<String> callbackCancelamentoDeConsulta
        );*/

        @GET("/associado/cancelamentoDeConsulta")
        void setCancelamentoDeConsulta(
                @Header("matricula") String matricula ,
                @Header("cdDependente") String cdDependente,
                @Header("id") int id,
                @Header("usuRede") String usuRede,
                @Header("usuSistema") String usuSistema,
                @Header("tipo") char tipo,
                @Header("solicitante") String solicitante,
                @Header("situacao") String situacao,
                @Header("horario") String horario,
                @Header("localidade") String localidade,
                @Header("especialidade") String especialidade,
                @Header("idAgenda") String idAgenda,
                Callback<String> callbackCancelamentoDeConsulta
        );


//        @POST("/associado/enviaEmail")
//        void enviaEmail(@Body EmailCanalAtendimento email, Callback<EmailCanalAtendimento> callback);

        @POST("/associado/enviaEmail")
        void enviaEmail(@Body EmailCanalAtendimento email, Callback<String> callback);


    }
}
