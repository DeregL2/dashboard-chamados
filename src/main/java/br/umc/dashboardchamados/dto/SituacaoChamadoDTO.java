package br.umc.dashboardchamados.dto;

import br.umc.dashboardchamados.model.StatusChamado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SituacaoChamadoDTO {

    private StatusChamado status;
    private long quantidade;
}
