package br.umc.dashboardchamados.dto;

import br.umc.dashboardchamados.model.PrioridadeChamado;
import br.umc.dashboardchamados.model.StatusChamado;
import br.umc.dashboardchamados.model.TipoChamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChamadoRequestDTO {

    @NotBlank
    private String titulo;

    private String descricao;

    @NotNull
    private TipoChamado tipo;

    @NotNull
    private StatusChamado status;

    @NotNull
    private PrioridadeChamado prioridade;

    @NotBlank
    private String solicitante;

    private String responsavel;

    private String consideracoesTecnicas;
}
