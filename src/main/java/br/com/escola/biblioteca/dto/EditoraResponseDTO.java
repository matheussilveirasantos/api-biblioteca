package br.com.escola.biblioteca.dto;

import br.com.escola.biblioteca.entity.Editora;

public record EditoraResponseDTO(Long id, String nome, String cnpj, String estado) {
    public static EditoraResponseDTO fromEntity(Editora e) {
        return new EditoraResponseDTO(e.getId(), e.getNome(), e.getCnpj(), e.getEstado());
    }
}
