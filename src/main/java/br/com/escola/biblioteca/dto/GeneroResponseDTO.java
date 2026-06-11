package br.com.escola.biblioteca.dto;

import br.com.escola.biblioteca.entity.Genero;

public record GeneroResponseDTO(
        Long id,
        String nome,
        String sigla
) {

    public static GeneroResponseDTO fromEntity(Genero genero) {
        return new GeneroResponseDTO(
                genero.getId(),
                genero.getNome(),
                genero.getSigla()
        );
    }
}
