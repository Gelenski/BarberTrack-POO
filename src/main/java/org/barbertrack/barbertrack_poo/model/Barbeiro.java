package org.barbertrack.barbertrack_poo.model;

import java.io.Serial;
import java.io.Serializable;

public class Barbeiro implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private String telefone;
    private String cpf;
    private String dataAdmissao;

    public Barbeiro(String nome,
                    String telefone,
                    String cpf,
                    String dataAdmissao) {

        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
        this.dataAdmissao = dataAdmissao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(String dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }
}