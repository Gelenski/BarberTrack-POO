package org.barbertrack.barbertrack_poo.model;

import java.io.Serial;
import java.io.Serializable;

public class Especialidade implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private String descricao;
    private int nivelHabilidade;

    public Especialidade(String nome, String descricao, int nivelHabilidade) throws Exception {
        setNome(nome);
        setDescricao(descricao);
        setNivelHabilidade(nivelHabilidade);
    }

    public String getNome() { return nome; }

    public void setNome(String nome) throws Exception {
        if (nome == null || nome.isBlank())
            throw new Exception("O nome da especialidade não pode ser vazio.");
        this.nome = nome;
    }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) throws Exception {
        if (descricao == null || descricao.isBlank())
            throw new Exception("A descrição não pode ser vazia.");
        this.descricao = descricao;
    }

    public int getNivelHabilidade() { return nivelHabilidade; }

    public void setNivelHabilidade(int nivelHabilidade) throws Exception {
        if (nivelHabilidade < 1)
            throw new Exception("O nível de habilidade deve ser maior ou igual a 1.");
        this.nivelHabilidade = nivelHabilidade;
    }
}
