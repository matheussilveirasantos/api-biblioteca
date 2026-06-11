package br.com.escola.biblioteca.service;

import br.com.escola.biblioteca.dto.RegisterRequestDTO;
import br.com.escola.biblioteca.dto.RegisterResponseDTO;
import br.com.escola.biblioteca.entity.Usuario;
import br.com.escola.biblioteca.exception.BusinessException;
import br.com.escola.biblioteca.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponseDTO registrar(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByUsername(dto.username())) {
            throw new BusinessException("Username '" + dto.username() + "' já está em uso");
        }
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Email '" + dto.email() + "' já está em uso");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setUsername(dto.username());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.password()));
        usuario.setRole("USER");

        Usuario salvo = usuarioRepository.save(usuario);
        return RegisterResponseDTO.of(salvo);
    }
}
