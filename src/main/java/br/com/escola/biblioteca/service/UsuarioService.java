package br.com.escola.biblioteca.service;

import br.com.escola.biblioteca.dto.CadastroRequestDTO;
import br.com.escola.biblioteca.dto.CadastroResponseDTO;
import br.com.escola.biblioteca.entity.RoleEnum;
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

    public CadastroResponseDTO cadastrar(CadastroRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Já existe um usuário cadastrado com este e-mail.");
        }

        Usuario usuario = new Usuario(
                dto.nome(),
                dto.email(),
                passwordEncoder.encode(dto.senha()),
                RoleEnum.USER
        );

        return CadastroResponseDTO.from(usuarioRepository.save(usuario));
    }
}
