package org.barbertrack.barbertrack_poo.model;

import java.io.Serial;
import java.io.Serializable;

public class HorarioFuncionamento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String diaSemana;
    private String horarioAbertura;
    private String horarioFechamento;

    public HorarioFuncionamento(String diaSemana, String horarioAbertura, String horarioFechamento) {
        this.diaSemana = diaSemana;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHorarioAbertura() {
        return horarioAbertura;
    }

    public void setHorarioAbertura(String horarioAbertura) {
        this.horarioAbertura = horarioAbertura;
    }

    public String getHorarioFechamento() {
        return horarioFechamento;
    }

    public void setHorarioFechamento(String horarioFechamento) {
        this.horarioFechamento = horarioFechamento;
    }
}
