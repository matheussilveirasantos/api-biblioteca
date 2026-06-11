package br.com.escola.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record AutorRequestDTO(
        @NotBlank(message = "O nome do autor é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "A nacionalidade é obrigatória")
        @Size(max = 50, message = "A nacionalidade deve ter no máximo 50 caracteres")
        String nacionalidade,

        @NotNull(message = "A data de nascimento é obrigatória")
        LocalDate dataNascimento
) {}
