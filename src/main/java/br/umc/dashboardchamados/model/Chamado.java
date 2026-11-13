package br.umc.dashboardchamados.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "chamado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoChamado tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusChamado status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadeChamado prioridade;

    @Column(nullable = false)
    private String solicitante;

    private String responsavel;

    @Column(columnDefinition = "TEXT")
    private String consideracoesTecnicas;

    @Column(nullable = false)
    private LocalDateTime dataAbertura;

    private LocalDateTime dataPrimeiraResposta;

    private LocalDateTime dataEncerramento;
}
