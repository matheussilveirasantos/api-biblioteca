package br.com.escola.biblioteca.dto;

import br.com.escola.biblioteca.entity.Livro;

public record LivroResponseDTO(
        Long id,
        String titulo,
        String isbn,
        Integer anoPublicacao,
        AutorResponseDTO autor,
        GeneroResponseDTO genero,
        EditoraResponseDTO editora
) {
    public static LivroResponseDTO fromEntity(Livro l) {
        return new LivroResponseDTO(
                l.getId(),
                l.getTitulo(),
                l.getIsbn(),
                l.getAnoPublicacao(),
                AutorResponseDTO.fromEntity(l.getAutor()),
                GeneroResponseDTO.fromEntity(l.getGenero()),
                EditoraResponseDTO.fromEntity(l.getEditora())
        );
    }
}
