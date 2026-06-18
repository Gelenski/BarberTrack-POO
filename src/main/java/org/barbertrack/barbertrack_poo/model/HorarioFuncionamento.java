package org.barbertrack.barbertrack_poo.model;

import java.io.Serial;
import java.io.Serializable;

public class HorarioFuncionamento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String diaSemana;
    private String horarioAbertura;
    private String horarioFechamento;

    public HorarioFuncionamento(String diaSemana, String horarioAbertura, String horarioFechamento) throws Exception {
        setDiaSemana(diaSemana);
        setHorarioAbertura(horarioAbertura);
        setHorarioFechamento(horarioFechamento);
    }

    public String getDiaSemana() { return diaSemana; }

    public void setDiaSemana(String diaSemana) throws Exception {
        if (diaSemana == null || diaSemana.isBlank())
            throw new Exception("O dia da semana não pode ser vazio.");
        this.diaSemana = diaSemana;
    }

    public String getHorarioAbertura() { return horarioAbertura; }

    public void setHorarioAbertura(String horarioAbertura) throws Exception {
        if (horarioAbertura == null || !horarioAbertura.matches("\\d{2}:\\d{2}"))
            throw new Exception("Horário de abertura inválido. Use o formato HH:mm.");
        this.horarioAbertura = horarioAbertura;
    }

    public String getHorarioFechamento() { return horarioFechamento; }

    public void setHorarioFechamento(String horarioFechamento) throws Exception {
        if (horarioFechamento == null || !horarioFechamento.matches("\\d{2}:\\d{2}"))
            throw new Exception("Horário de fechamento inválido. Use o formato HH:mm.");
        this.horarioFechamento = horarioFechamento;
    }
}
