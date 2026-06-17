package org.barbertrack.barbertrack_poo.model;

import java.io.Serial;
import java.io.Serializable;

public class CategoriaServico implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private String descricao;
    private boolean status; // True -> Ativo/False -> Inativo

    public CategoriaServico(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.status = true;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return nome;
    }
}
