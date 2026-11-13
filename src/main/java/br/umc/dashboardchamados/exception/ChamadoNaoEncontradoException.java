package br.umc.dashboardchamados.exception;

public class ChamadoNaoEncontradoException extends RuntimeException {

    public ChamadoNaoEncontradoException(Long id) {
        super("Chamado não encontrado com id " + id);
    }
}
