package org.barbertrack.barbertrack_poo.model;

import java.io.Serial;
import java.io.Serializable;

public class Barbearia implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String razaoSocial;
    private String nomeFantasia;
    private String email;

    public Barbearia(String razaoSocial, String nomeFantasia, String email) throws Exception {
        setRazaoSocial(razaoSocial);
        setNomeFantasia(nomeFantasia);
        setEmail(email);
    }

    public String getRazaoSocial() { return razaoSocial; }

    public void setRazaoSocial(String razaoSocial) throws Exception {
        if (razaoSocial == null || razaoSocial.isBlank())
            throw new Exception("A razão social não pode ser vazia.");
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() { return nomeFantasia; }

    public void setNomeFantasia(String nomeFantasia) throws Exception {
        if (nomeFantasia == null || nomeFantasia.isBlank())
            throw new Exception("O nome fantasia não pode ser vazio.");
        this.nomeFantasia = nomeFantasia;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) throws Exception {
        if (email == null || !email.contains("@"))
            throw new Exception("E-mail inválido.");
        this.email = email;
    }
}
