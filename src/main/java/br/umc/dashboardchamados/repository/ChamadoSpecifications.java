package br.umc.dashboardchamados.repository;

import br.umc.dashboardchamados.dto.ChamadoFiltroDTO;
import br.umc.dashboardchamados.model.Chamado;
import br.umc.dashboardchamados.model.PrioridadeChamado;
import br.umc.dashboardchamados.model.StatusChamado;
import br.umc.dashboardchamados.model.TipoChamado;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class ChamadoSpecifications {

    private ChamadoSpecifications() {
    }

    public static Specification<Chamado> comFiltro(ChamadoFiltroDTO filtro) {
        return Specification.where(dataInicioMaiorOuIgual(filtro.getDataInicio()))
                .and(dataFimMenorOuIgual(filtro.getDataFim()))
                .and(statusIgual(filtro.getStatus()))
                .and(tipoIgual(filtro.getTipo()))
                .and(prioridadeIgual(filtro.getPrioridade()));
    }

    private static Specification<Chamado> dataInicioMaiorOuIgual(LocalDate dataInicio) {
        return (root, query, builder) -> dataInicio == null
                ? null
                : builder.greaterThanOrEqualTo(root.get("dataAbertura"), dataInicio.atStartOfDay());
    }

    private static Specification<Chamado> dataFimMenorOuIgual(LocalDate dataFim) {
        return (root, query, builder) -> dataFim == null
                ? null
                : builder.lessThanOrEqualTo(root.get("dataAbertura"), dataFim.atTime(23, 59, 59));
    }

    private static Specification<Chamado> statusIgual(StatusChamado status) {
        return (root, query, builder) -> status == null
                ? null
                : builder.equal(root.get("status"), status);
    }

    private static Specification<Chamado> tipoIgual(TipoChamado tipo) {
        return (root, query, builder) -> tipo == null
                ? null
                : builder.equal(root.get("tipo"), tipo);
    }

    private static Specification<Chamado> prioridadeIgual(PrioridadeChamado prioridade) {
        return (root, query, builder) -> prioridade == null
                ? null
                : builder.equal(root.get("prioridade"), prioridade);
    }
}
