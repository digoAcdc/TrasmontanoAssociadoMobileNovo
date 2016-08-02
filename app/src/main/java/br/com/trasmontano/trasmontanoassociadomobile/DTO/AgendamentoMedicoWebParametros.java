package br.com.trasmontano.trasmontanoassociadomobile.DTO;

/**
 * Created by rbarbosa on 02/08/2016.
 */
public class AgendamentoMedicoWebParametros {
    private String cbos;
    private String especialidade;

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCbos() {
        return cbos;
    }

    public void setCbos(String cbos) {
        this.cbos = cbos;
    }

    private String localidade;

}
