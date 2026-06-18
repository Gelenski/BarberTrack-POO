package org.barbertrack.barbertrack_poo.model;

import java.io.Serial;
import java.io.Serializable;

public class Servico implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private int duracao; // Minutos
    private boolean status = true; // True -> Ativo / False -> Inativo
    private CategoriaServico categoriaServico;

    public Servico(String nome, int duracao, CategoriaServico categoriaServico) throws Exception {
        setNome(nome);
        setDuracao(duracao);
        this.categoriaServico = categoriaServico;
    }

    public String getNome() { return nome; }

    public void setNome(String nome) throws Exception {
        if (nome == null || nome.isBlank())
            throw new Exception("O nome do serviço não pode ser vazio.");
        this.nome = nome;
    }

    public int getDuracao() { return duracao; }

    public void setDuracao(int duracao) throws Exception {
        if (duracao <= 0)
            throw new Exception("A duração deve ser maior que zero.");
        this.duracao = duracao;
    }

    public boolean isStatus() { return status; }

    public void setStatus(boolean status) { this.status = status; }

    public CategoriaServico getCategoriaServico() { return categoriaServico; }

    public void setCategoriaServico(CategoriaServico categoriaServico) {
        this.categoriaServico = categoriaServico;
    }

    @Override
    public String toString() { return nome; }
}
