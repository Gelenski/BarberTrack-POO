package org.barbertrack.barbertrack_poo.model;

import java.io.Serial;
import java.io.Serializable;

public class Especialidade implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private String descricao;
    private int nivelHabilidade;

    public Especialidade(String nome, String descricao, int nivelHabilidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.nivelHabilidade = nivelHabilidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getNivelHabilidade() {
        return nivelHabilidade;
    }

    public void setNivelHabilidade(int nivelHabilidade) {
        this.nivelHabilidade = nivelHabilidade;
    }
}