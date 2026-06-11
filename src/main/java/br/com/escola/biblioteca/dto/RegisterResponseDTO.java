package br.com.escola.biblioteca.dto;

public record RegisterResponseDTO(Long id, String nome, String username, String email, String role) {
    public static RegisterResponseDTO of(br.com.escola.biblioteca.entity.Usuario u) {
        return new RegisterResponseDTO(u.getId(), u.getNome(), u.getUsername(), u.getEmail(), u.getRole());
    }
}
