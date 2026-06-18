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

    public Barbeiro(String nome, String telefone, String cpf, String dataAdmissao) throws Exception {
        setNome(nome);
        setTelefone(telefone);
        setCpf(cpf);
        setDataAdmissao(dataAdmissao);
    }

    public String getNome() { return nome; }

    public void setNome(String nome) throws Exception {
        if (nome == null || nome.isBlank())
            throw new Exception("O nome do barbeiro não pode ser vazio.");
        this.nome = nome;
    }

    public String getTelefone() { return telefone; }

    public void setTelefone(String telefone) throws Exception {
        if (telefone == null || telefone.isBlank())
            throw new Exception("O telefone não pode ser vazio.");
        this.telefone = telefone;
    }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) throws Exception {
        if (cpf == null || !cpf.matches("\\d{11}"))
            throw new Exception("CPF inválido. Deve conter exatamente 11 dígitos.");
        this.cpf = cpf;
    }

    public String getDataAdmissao() { return dataAdmissao; }

    public void setDataAdmissao(String dataAdmissao) throws Exception {
        if (dataAdmissao == null || !dataAdmissao.matches("\\d{2}/\\d{2}/\\d{4}"))
            throw new Exception("Data de admissão inválida. Use o formato DD/MM/AAAA.");
        this.dataAdmissao = dataAdmissao;
    }
}
