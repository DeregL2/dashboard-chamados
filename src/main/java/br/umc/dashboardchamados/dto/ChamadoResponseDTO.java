package br.umc.dashboardchamados.dto;

import br.umc.dashboardchamados.model.PrioridadeChamado;
import br.umc.dashboardchamados.model.StatusChamado;
import br.umc.dashboardchamados.model.TipoChamado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChamadoResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private TipoChamado tipo;
    private StatusChamado status;
    private PrioridadeChamado prioridade;
    private String solicitante;
    private String responsavel;
    private String consideracoesTecnicas;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataPrimeiraResposta;
    private LocalDateTime dataEncerramento;
}
