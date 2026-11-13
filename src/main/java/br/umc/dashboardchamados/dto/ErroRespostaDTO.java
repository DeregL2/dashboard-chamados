package br.umc.dashboardchamados.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ErroRespostaDTO {

    private LocalDateTime timestamp;
    private int status;
    private String mensagem;
}
