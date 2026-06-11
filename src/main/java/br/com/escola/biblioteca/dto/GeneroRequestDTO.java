package br.com.escola.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GeneroRequestDTO(

    @NotBlank(message = "O campo 'nome' é obrigatório.")
    @Size(max = 100, message = "O campo 'nome' deve conter no máximo 100 caracteres.")
    String nome,

    @NotBlank(message = "A sigla é obrigatória.")
    @Size(min = 3, max = 3, message = "A sigla deve conter 3 caracteres.")
        String sigla
) {



}
