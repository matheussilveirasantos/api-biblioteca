package br.com.escola.biblioteca.dto;

public record LoginResponseDTO(String token, String tipo, String username) {
    public static LoginResponseDTO of(String token, String username) {
        return new LoginResponseDTO(token, "Bearer", username);
    }
}
