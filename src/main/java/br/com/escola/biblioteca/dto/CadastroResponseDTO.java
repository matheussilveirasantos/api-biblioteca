package br.com.escola.biblioteca.dto;

import br.com.escola.biblioteca.entity.Usuario;

import java.time.LocalDateTime;

public record CadastroResponseDTO(
        Long id,
        String nome,
        String email,
        String role,
        LocalDateTime criadoEm
) {
    public static CadastroResponseDTO from(Usuario usuario) {
        return new CadastroResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole().name(),
                usuario.getCriadoEm()
        );
    }
}
