package org.barbertrack.barbertrack_poo.model;

import java.io.Serial;
import java.io.Serializable;

public class Cliente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private String telefone;
    private String email;

    public Cliente(String nome, String telefone, String email) throws Exception {
        setNome(nome);
        setTelefone(telefone);
        setEmail(email);
    }

    public String getNome() { return nome; }

    public void setNome(String nome) throws Exception {
        if (nome == null || nome.isBlank())
            throw new Exception("O nome do cliente não pode ser vazio.");
        this.nome = nome;
    }

    public String getTelefone() { return telefone; }

    public void setTelefone(String telefone) throws Exception {
        if (telefone == null || !telefone.matches("\\d+"))
            throw new Exception("O telefone deve conter apenas números.");
        this.telefone = telefone;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) throws Exception {
        if (email == null || !email.contains("@"))
            throw new Exception("E-mail inválido.");
        this.email = email;
    }

    @Override
    public String toString() { return nome; }
}
