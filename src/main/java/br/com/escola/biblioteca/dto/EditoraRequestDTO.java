package br.com.escola.biblioteca.dto;

import br.com.escola.biblioteca.validation.CnpjValido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditoraRequestDTO(
        @NotBlank(message = "O nome da editora é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "O CNPJ é obrigatório")
        @CnpjValido
        String cnpj,

        @NotBlank(message = "O estado é obrigatório")
        @Size(min = 2, max = 2, message = "O estado deve ter exatamente 2 caracteres (ex: RJ, SP, POA use RS)")
        String estado
) {}
