package br.umc.dashboardchamados.security;

import br.umc.dashboardchamados.model.Usuario;
import br.umc.dashboardchamados.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOuEmail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(usernameOuEmail)
                .or(() -> usuarioRepository.findByEmail(usernameOuEmail))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Nenhum usuário encontrado com login/e-mail: " + usernameOuEmail));

        return new UsuarioDetails(usuario);
    }
}
