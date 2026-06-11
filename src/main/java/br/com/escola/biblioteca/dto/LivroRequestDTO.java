package br.com.escola.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LivroRequestDTO(
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
        String titulo,

        @NotBlank(message = "O ISBN é obrigatório")
        @Size(max = 13, message = "O ISBN deve ter no máximo 13 caracteres")
        String isbn,

        @NotNull(message = "O ano de publicação é obrigatório")
        Integer anoPublicacao,

        @NotNull(message = "O ID do autor é obrigatório")
        Long autorId,

        @NotNull(message = "O ID do gênero é obrigatório")
        Long generoId,

        @NotNull(message = "O ID da editora é obrigatório")
        Long editoraId
) {}
