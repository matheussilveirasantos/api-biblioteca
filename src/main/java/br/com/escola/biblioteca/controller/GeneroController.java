package br.com.escola.biblioteca.controller;

import br.com.escola.biblioteca.dto.GeneroRequestDTO;
import br.com.escola.biblioteca.dto.GeneroResponseDTO;
import br.com.escola.biblioteca.service.GeneroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/generos")
@Tag(name = "Gêneros", description = "CRUD de gêneros literários")
@SecurityRequirement(name = "BearerAuth")
public class GeneroController {

    private final GeneroService generoService;

    public GeneroController(GeneroService generoService) {
        this.generoService = generoService;
    }

    @Operation(summary = "Listar gêneros")
    @GetMapping
    public ResponseEntity<List<GeneroResponseDTO>> listar() {
        return ResponseEntity.ok(generoService.listar());
    }

    @Operation(summary = "Buscar gênero por ID")
    @GetMapping("/{id}")
    public ResponseEntity<GeneroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(generoService.buscarPorId(id));
    }

    @Operation(summary = "Criar gênero", description = "Exemplo: {nome: Romance, sigla: ROM}")
    @PostMapping
    public ResponseEntity<GeneroResponseDTO> criar(@Valid @RequestBody GeneroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(generoService.criar(dto));
    }

    @Operation(summary = "Atualizar gênero")
    @PutMapping("/{id}")
    public ResponseEntity<GeneroResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody GeneroRequestDTO dto) {
        return ResponseEntity.ok(generoService.atualizar(id, dto));
    }

    @Operation(summary = "Deletar gênero", description = "Não é permitido deletar um gênero que possua livros vinculados")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        generoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
