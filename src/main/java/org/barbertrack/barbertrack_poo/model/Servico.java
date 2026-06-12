package org.barbertrack.barbertrack_poo.model;

public class Servico {

    private String nome;
    private int duracao; // Minutos
    private boolean status = true; // True -> Ativo/False -> Inativo

    public Servico(String nome, int duracao) {
        this.nome = nome;
        this.duracao = duracao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
