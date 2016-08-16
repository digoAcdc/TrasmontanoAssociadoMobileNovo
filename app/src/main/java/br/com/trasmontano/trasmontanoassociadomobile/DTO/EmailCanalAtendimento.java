package br.com.trasmontano.trasmontanoassociadomobile.DTO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rbarbosa on 16/08/2016.
 */
public class EmailCanalAtendimento {


    private String nome;
    private String matricula;
    private String cdDependente;
    private String email;
    private String assunto;
    private String mensagem;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCdDependente() {
        return cdDependente;
    }

    public void setCdDependente(String cdDependente) {
        this.cdDependente = cdDependente;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}
