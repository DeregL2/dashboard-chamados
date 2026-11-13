package br.umc.dashboardchamados.controller;

import br.umc.dashboardchamados.dto.AtualizarStatusRequestDTO;
import br.umc.dashboardchamados.dto.ChamadoFiltroDTO;
import br.umc.dashboardchamados.dto.ChamadoPorPeriodoDTO;
import br.umc.dashboardchamados.dto.ChamadoRequestDTO;
import br.umc.dashboardchamados.dto.ChamadoResponseDTO;
import br.umc.dashboardchamados.dto.IndicadoresDTO;
import br.umc.dashboardchamados.dto.SituacaoChamadoDTO;
import br.umc.dashboardchamados.dto.TipoMaisAtendidoDTO;
import br.umc.dashboardchamados.model.AgrupamentoPeriodo;
import br.umc.dashboardchamados.model.PrioridadeChamado;
import br.umc.dashboardchamados.model.StatusChamado;
import br.umc.dashboardchamados.model.TipoChamado;
import br.umc.dashboardchamados.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    private final ChamadoService service;

    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ChamadoResponseDTO> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) StatusChamado status,
            @RequestParam(required = false) TipoChamado tipo,
            @RequestParam(required = false) PrioridadeChamado prioridade) {
        return service.listar(montarFiltro(dataInicio, dataFim, status, tipo, prioridade));
    }

    @GetMapping("/{id}")
    public ChamadoResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ChamadoResponseDTO> criar(@Valid @RequestBody ChamadoRequestDTO dto) {
        return ResponseEntity.ok(service.criar(dto));
    }

    @PutMapping("/{id}")
    public ChamadoResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody ChamadoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @PatchMapping("/{id}/status")
    public ChamadoResponseDTO atualizarStatus(@PathVariable Long id, @Valid @RequestBody AtualizarStatusRequestDTO dto) {
        return service.atualizarStatus(id, dto.getStatus());
    }

    @GetMapping("/indicadores")
    public IndicadoresDTO indicadores(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) StatusChamado status,
            @RequestParam(required = false) TipoChamado tipo,
            @RequestParam(required = false) PrioridadeChamado prioridade) {
        return service.indicadores(montarFiltro(dataInicio, dataFim, status, tipo, prioridade));
    }

    @GetMapping("/por-periodo")
    public List<ChamadoPorPeriodoDTO> porPeriodo(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) StatusChamado status,
            @RequestParam(required = false) TipoChamado tipo,
            @RequestParam(required = false) PrioridadeChamado prioridade,
            @RequestParam(defaultValue = "DIA") AgrupamentoPeriodo agrupamento) {
        return service.porPeriodo(montarFiltro(dataInicio, dataFim, status, tipo, prioridade), agrupamento);
    }

    @GetMapping("/situacao")
    public List<SituacaoChamadoDTO> situacao(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) TipoChamado tipo,
            @RequestParam(required = false) PrioridadeChamado prioridade) {
        return service.situacao(montarFiltro(dataInicio, dataFim, null, tipo, prioridade));
    }

    @GetMapping("/tipos-mais-atendidos")
    public List<TipoMaisAtendidoDTO> tiposMaisAtendidos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) StatusChamado status,
            @RequestParam(required = false) PrioridadeChamado prioridade,
            @RequestParam(defaultValue = "5") int topN) {
        return service.tiposMaisAtendidos(montarFiltro(dataInicio, dataFim, status, null, prioridade), topN);
    }

    private ChamadoFiltroDTO montarFiltro(LocalDate dataInicio, LocalDate dataFim, StatusChamado status,
                                           TipoChamado tipo, PrioridadeChamado prioridade) {
        ChamadoFiltroDTO filtro = new ChamadoFiltroDTO();
        filtro.setDataInicio(dataInicio);
        filtro.setDataFim(dataFim);
        filtro.setStatus(status);
        filtro.setTipo(tipo);
        filtro.setPrioridade(prioridade);
        return filtro;
    }
}
