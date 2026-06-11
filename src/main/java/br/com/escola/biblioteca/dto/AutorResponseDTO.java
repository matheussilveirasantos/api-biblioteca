package br.com.escola.biblioteca.dto;

import br.com.escola.biblioteca.entity.Autor;
import java.time.LocalDate;

public record AutorResponseDTO(Long id, String nome, String nacionalidade, LocalDate dataNascimento) {
    public static AutorResponseDTO fromEntity(Autor a) {
        return new AutorResponseDTO(a.getId(), a.getNome(), a.getNacionalidade(), a.getDataNascimento());
    }
}
