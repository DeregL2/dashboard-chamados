package br.umc.dashboardchamados.model;

public enum PerfilAcesso {
    ADMIN("Administrador");

    private final String label;

    PerfilAcesso(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getAuthority() {
        return "ROLE_" + name();
    }
}
