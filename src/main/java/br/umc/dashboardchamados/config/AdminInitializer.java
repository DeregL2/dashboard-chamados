package br.umc.dashboardchamados.config;

import br.umc.dashboardchamados.model.PerfilAcesso;
import br.umc.dashboardchamados.model.Usuario;
import br.umc.dashboardchamados.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminEmail;
    private final String adminPassword;

    public AdminInitializer(UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder,
                             @Value("${app.admin.username}") String adminUsername,
                             @Value("${app.admin.email}") String adminEmail,
                             @Value("${app.admin.password}") String adminPassword) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername(adminUsername);
            admin.setEmail(adminEmail.toLowerCase());
            admin.setNomeCompleto("Administrador do Dashboard");
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setPerfis(Set.of(PerfilAcesso.ADMIN));
            admin.setEnabled(true);
            usuarioRepository.save(admin);
            log.warn("Usuário ADMIN inicial criado (username='{}'). Troque a senha padrão assim que fizer o primeiro login!", adminUsername);
        }
    }
}
