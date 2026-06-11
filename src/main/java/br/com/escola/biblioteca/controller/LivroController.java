package br.com.escola.biblioteca.controller;

import br.com.escola.biblioteca.dto.LivroRequestDTO;
import br.com.escola.biblioteca.dto.LivroResponseDTO;
import br.com.escola.biblioteca.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
@Tag(name = "Livros", description = "CRUD de livros — requer autorId, generoId e editoraId")
@SecurityRequirement(name = "BearerAuth")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @Operation(summary = "Cadastrar livro",
               description = "Cria um livro. autorId, generoId e editoraId são obrigatórios. " +
                             "Dispara e-mail de notificação.")
    @PostMapping
    public ResponseEntity<LivroResponseDTO> criar(@Valid @RequestBody LivroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.criar(dto));
    }

    @Operation(summary = "Cadastrar livros em lote")
    @PostMapping("/lote")
    public ResponseEntity<List<LivroResponseDTO>> criarLote(@Valid @RequestBody List<LivroRequestDTO> dtos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.criarLote(dtos));
    }

    @Operation(summary = "Listar todos os livros")
    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listarTodos() {
        return ResponseEntity.ok(livroService.listarTodos());
    }

    @Operation(summary = "Buscar livro por ID")
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar livro",
               description = "Atualiza dados do livro. Dispara e-mail de notificação.")
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(@PathVariable Long id,
                                                       @Valid @RequestBody LivroRequestDTO dto) {
        return ResponseEntity.ok(livroService.atualizar(id, dto));
    }

    @Operation(summary = "Deletar livro",
               description = "Remove o livro. Dispara e-mail de notificação.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
