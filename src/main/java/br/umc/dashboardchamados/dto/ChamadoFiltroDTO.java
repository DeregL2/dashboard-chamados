package br.umc.dashboardchamados.dto;

import br.umc.dashboardchamados.model.PrioridadeChamado;
import br.umc.dashboardchamados.model.StatusChamado;
import br.umc.dashboardchamados.model.TipoChamado;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ChamadoFiltroDTO {

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private StatusChamado status;
    private TipoChamado tipo;
    private PrioridadeChamado prioridade;
}
