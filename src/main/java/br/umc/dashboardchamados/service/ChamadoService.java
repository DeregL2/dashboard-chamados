package br.umc.dashboardchamados.service;

import br.umc.dashboardchamados.dto.ChamadoFiltroDTO;
import br.umc.dashboardchamados.dto.ChamadoPorPeriodoDTO;
import br.umc.dashboardchamados.dto.ChamadoRequestDTO;
import br.umc.dashboardchamados.dto.ChamadoResponseDTO;
import br.umc.dashboardchamados.dto.IndicadoresDTO;
import br.umc.dashboardchamados.dto.SituacaoChamadoDTO;
import br.umc.dashboardchamados.dto.TipoMaisAtendidoDTO;
import br.umc.dashboardchamados.exception.ChamadoNaoEncontradoException;
import br.umc.dashboardchamados.model.AgrupamentoPeriodo;
import br.umc.dashboardchamados.model.Chamado;
import br.umc.dashboardchamados.model.StatusChamado;
import br.umc.dashboardchamados.model.TipoChamado;
import br.umc.dashboardchamados.repository.ChamadoRepository;
import br.umc.dashboardchamados.repository.ChamadoSpecifications;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ChamadoService {

    private final ChamadoRepository repository;

    public ChamadoService(ChamadoRepository repository) {
        this.repository = repository;
    }

    public List<ChamadoResponseDTO> listar(ChamadoFiltroDTO filtro) {
        return repository.findAll(ChamadoSpecifications.comFiltro(filtro)).stream()
                .map(this::paraResponseDTO)
                .collect(Collectors.toList());
    }

    public ChamadoResponseDTO buscarPorId(Long id) {
        return paraResponseDTO(buscarEntidadePorId(id));
    }

    public ChamadoResponseDTO criar(ChamadoRequestDTO dto) {
        Chamado chamado = Chamado.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .tipo(dto.getTipo())
                .status(dto.getStatus())
                .prioridade(dto.getPrioridade())
                .solicitante(dto.getSolicitante())
                .responsavel(dto.getResponsavel())
                .consideracoesTecnicas(dto.getConsideracoesTecnicas())
                .dataAbertura(LocalDateTime.now())
                .build();
        return paraResponseDTO(repository.save(chamado));
    }

    public ChamadoResponseDTO atualizar(Long id, ChamadoRequestDTO dto) {
        Chamado chamado = buscarEntidadePorId(id);
        chamado.setTitulo(dto.getTitulo());
        chamado.setDescricao(dto.getDescricao());
        chamado.setTipo(dto.getTipo());
        chamado.setStatus(dto.getStatus());
        chamado.setPrioridade(dto.getPrioridade());
        chamado.setSolicitante(dto.getSolicitante());
        chamado.setResponsavel(dto.getResponsavel());
        chamado.setConsideracoesTecnicas(dto.getConsideracoesTecnicas());
        return paraResponseDTO(repository.save(chamado));
    }

    public ChamadoResponseDTO atualizarStatus(Long id, StatusChamado novoStatus) {
        Chamado chamado = buscarEntidadePorId(id);

        if (chamado.getStatus() == StatusChamado.ABERTO && novoStatus != StatusChamado.ABERTO
                && chamado.getDataPrimeiraResposta() == null) {
            chamado.setDataPrimeiraResposta(LocalDateTime.now());
        }

        if (novoStatus == StatusChamado.RESOLVIDO || novoStatus == StatusChamado.CANCELADO) {
            chamado.setDataEncerramento(LocalDateTime.now());
        }

        chamado.setStatus(novoStatus);
        return paraResponseDTO(repository.save(chamado));
    }

    public IndicadoresDTO indicadores(ChamadoFiltroDTO filtro) {
        List<Chamado> chamados = repository.findAll(ChamadoSpecifications.comFiltro(filtro));

        long totalAbertos = contarPorStatus(chamados, StatusChamado.ABERTO);
        long totalEmAndamento = contarPorStatus(chamados, StatusChamado.EM_ANDAMENTO);
        long totalResolvidos = contarPorStatus(chamados, StatusChamado.RESOLVIDO);
        long totalCancelados = contarPorStatus(chamados, StatusChamado.CANCELADO);

        OptionalDouble mediaAtendimentoMinutos = chamados.stream()
                .filter(c -> c.getDataPrimeiraResposta() != null)
                .mapToLong(c -> Duration.between(c.getDataAbertura(), c.getDataPrimeiraResposta()).toMinutes())
                .average();

        OptionalDouble mediaResolucaoMinutos = chamados.stream()
                .filter(c -> c.getStatus() == StatusChamado.RESOLVIDO && c.getDataEncerramento() != null)
                .mapToLong(c -> Duration.between(c.getDataAbertura(), c.getDataEncerramento()).toMinutes())
                .average();

        Double tempoMedioAtendimentoHoras = mediaAtendimentoMinutos.isPresent()
                ? mediaAtendimentoMinutos.getAsDouble() / 60.0
                : null;

        Double tempoMedioResolucaoHoras = mediaResolucaoMinutos.isPresent()
                ? mediaResolucaoMinutos.getAsDouble() / 60.0
                : null;

        return IndicadoresDTO.builder()
                .totalChamados(chamados.size())
                .totalAbertos(totalAbertos)
                .totalEmAndamento(totalEmAndamento)
                .totalResolvidos(totalResolvidos)
                .totalCancelados(totalCancelados)
                .tempoMedioAtendimentoHoras(tempoMedioAtendimentoHoras)
                .tempoMedioResolucaoHoras(tempoMedioResolucaoHoras)
                .build();
    }

    public List<ChamadoPorPeriodoDTO> porPeriodo(ChamadoFiltroDTO filtro, AgrupamentoPeriodo agrupamento) {
        List<Chamado> chamados = repository.findAll(ChamadoSpecifications.comFiltro(filtro));

        Map<LocalDate, Long> abertosPorPeriodo = chamados.stream()
                .collect(Collectors.groupingBy(
                        c -> truncarData(c.getDataAbertura().toLocalDate(), agrupamento),
                        Collectors.counting()));

        Map<LocalDate, Long> resolvidosPorPeriodo = chamados.stream()
                .filter(c -> c.getStatus() == StatusChamado.RESOLVIDO && c.getDataEncerramento() != null)
                .collect(Collectors.groupingBy(
                        c -> truncarData(c.getDataEncerramento().toLocalDate(), agrupamento),
                        Collectors.counting()));

        Map<LocalDate, long[]> consolidado = new TreeMap<>();

        abertosPorPeriodo.forEach((periodo, quantidade) ->
                consolidado.computeIfAbsent(periodo, chave -> new long[2])[0] = quantidade);

        resolvidosPorPeriodo.forEach((periodo, quantidade) ->
                consolidado.computeIfAbsent(periodo, chave -> new long[2])[1] = quantidade);

        List<ChamadoPorPeriodoDTO> resultado = new ArrayList<>();
        consolidado.forEach((periodo, valores) -> resultado.add(ChamadoPorPeriodoDTO.builder()
                .periodo(periodo)
                .abertos(valores[0])
                .resolvidos(valores[1])
                .build()));

        return resultado;
    }

    public List<SituacaoChamadoDTO> situacao(ChamadoFiltroDTO filtro) {
        List<Chamado> chamados = repository.findAll(ChamadoSpecifications.comFiltro(filtro));
        Map<StatusChamado, Long> contagem = chamados.stream()
                .collect(Collectors.groupingBy(Chamado::getStatus, Collectors.counting()));

        return Arrays.stream(StatusChamado.values())
                .map(status -> SituacaoChamadoDTO.builder()
                        .status(status)
                        .quantidade(contagem.getOrDefault(status, 0L))
                        .build())
                .collect(Collectors.toList());
    }

    public List<TipoMaisAtendidoDTO> tiposMaisAtendidos(ChamadoFiltroDTO filtro, int topN) {
        List<Chamado> chamados = repository.findAll(ChamadoSpecifications.comFiltro(filtro));
        Map<TipoChamado, Long> contagem = chamados.stream()
                .collect(Collectors.groupingBy(Chamado::getTipo, Collectors.counting()));

        return contagem.entrySet().stream()
                .sorted(Map.Entry.<TipoChamado, Long>comparingByValue().reversed())
                .limit(topN)
                .map(entrada -> TipoMaisAtendidoDTO.builder()
                        .tipo(entrada.getKey())
                        .quantidade(entrada.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private LocalDate truncarData(LocalDate data, AgrupamentoPeriodo agrupamento) {
        return switch (agrupamento) {
            case DIA -> data;
            case SEMANA -> data.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            case MES -> data.withDayOfMonth(1);
        };
    }

    private long contarPorStatus(List<Chamado> chamados, StatusChamado status) {
        return chamados.stream().filter(c -> c.getStatus() == status).count();
    }

    private Chamado buscarEntidadePorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new ChamadoNaoEncontradoException(id));
    }

    private ChamadoResponseDTO paraResponseDTO(Chamado chamado) {
        return ChamadoResponseDTO.builder()
                .id(chamado.getId())
                .titulo(chamado.getTitulo())
                .descricao(chamado.getDescricao())
                .tipo(chamado.getTipo())
                .status(chamado.getStatus())
                .prioridade(chamado.getPrioridade())
                .solicitante(chamado.getSolicitante())
                .responsavel(chamado.getResponsavel())
                .consideracoesTecnicas(chamado.getConsideracoesTecnicas())
                .dataAbertura(chamado.getDataAbertura())
                .dataPrimeiraResposta(chamado.getDataPrimeiraResposta())
                .dataEncerramento(chamado.getDataEncerramento())
                .build();
    }
}
