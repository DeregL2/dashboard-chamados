package br.umc.dashboardchamados.repository;

import br.umc.dashboardchamados.model.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChamadoRepository extends JpaRepository<Chamado, Long>, JpaSpecificationExecutor<Chamado> {
}
