package org.barbertrack.barbertrack_poo.model;

import java.io.Serial;
import java.io.Serializable;

public class Barbearia implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    private String razaoSocial;
    private String nomeFantasia;
    private String email;

    public Barbearia(String razaoSocial, String nomeFantasia, String email) {
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.email = email;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
