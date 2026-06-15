package org.barbertrack.barbertrack_poo.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Agendamento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDateTime dataAgendamento;
    private Cliente cliente;
    private Servico servico;

    public Agendamento(LocalDateTime dataAgendamento, Cliente cliente, Servico servico) {
        this.dataAgendamento = dataAgendamento;
        this.cliente = cliente;
        this.servico = servico;
    }

    public LocalDateTime getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDateTime dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }
}

