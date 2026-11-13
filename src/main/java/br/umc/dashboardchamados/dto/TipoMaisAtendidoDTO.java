package br.umc.dashboardchamados.dto;

import br.umc.dashboardchamados.model.TipoChamado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TipoMaisAtendidoDTO {

    private TipoChamado tipo;
    private long quantidade;
}
