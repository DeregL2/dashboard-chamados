package br.umc.dashboardchamados.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IndicadoresDTO {

    private long totalChamados;
    private long totalAbertos;
    private long totalEmAndamento;
    private long totalResolvidos;
    private long totalCancelados;
    private Double tempoMedioAtendimentoHoras;
    private Double tempoMedioResolucaoHoras;
}
