package br.umc.dashboardchamados.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "usuarios")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String nomeCompleto;

    private Set<PerfilAcesso> perfis = new HashSet<>();

    private boolean enabled = true;

    @CreatedDate
    private Instant createdAt;

    public Usuario() {
    }

    public Usuario(String username, String email, String password, String nomeCompleto, Set<PerfilAcesso> perfis) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nomeCompleto = nomeCompleto;
        this.perfis = perfis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public Set<PerfilAcesso> getPerfis() {
        return perfis;
    }

    public void setPerfis(Set<PerfilAcesso> perfis) {
        this.perfis = perfis;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean hasPerfil(PerfilAcesso perfil) {
        return perfis != null && perfis.contains(perfil);
    }
}
