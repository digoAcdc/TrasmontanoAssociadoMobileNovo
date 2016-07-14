package br.com.trasmontano.trasmontanoassociadomobile.DTO;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.AutoIncrement;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Key;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * Created by rbarbosa on 07/07/2016.
 */
@Table("associado")
public class Associado extends Model {

    @Key
    @Column("id")
    @AutoIncrement
    private long id;
    @Column("usuario")
    private String usuario;
    @Column("nome")
    private String nome;
    @Column("caminhoImagem")
    private String caminhoImagem;
    @Column("dataNascimento")
    private String dataNascimento;
    @Column("cpf")
    private String cpf;

   /* private String senha;
    private String email;
    private String lembreteSenha;*/

    public long getId() {
        return id;
    }

    public void setId(long caminhoImagem) {
        this.id = id;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

   /* public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }*/

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /*public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLembreteSenha() {
        return lembreteSenha;
    }

    public void setLembreteSenha(String lembreteSenha) {
        this.lembreteSenha = lembreteSenha;
    }*/
}
