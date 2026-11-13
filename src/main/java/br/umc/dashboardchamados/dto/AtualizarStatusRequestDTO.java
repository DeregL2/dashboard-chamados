package br.umc.dashboardchamados.dto;

import br.umc.dashboardchamados.model.StatusChamado;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarStatusRequestDTO {

    @NotNull
    private StatusChamado status;
}
