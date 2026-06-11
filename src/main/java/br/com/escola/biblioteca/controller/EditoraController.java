package br.com.escola.biblioteca.controller;

import br.com.escola.biblioteca.dto.EditoraRequestDTO;
import br.com.escola.biblioteca.dto.EditoraResponseDTO;
import br.com.escola.biblioteca.service.EditoraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/editoras")
@Tag(name = "Editoras", description = "CRUD de editoras")
@SecurityRequirement(name = "BearerAuth")
public class EditoraController {

    private final EditoraService editoraService;

    public EditoraController(EditoraService editoraService) {
        this.editoraService = editoraService;
    }

    @Operation(summary = "Listar editoras")
    @GetMapping
    public ResponseEntity<List<EditoraResponseDTO>> listar() {
        return ResponseEntity.ok(editoraService.listar());
    }

    @Operation(summary = "Buscar editora por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EditoraResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(editoraService.buscarPorId(id));
    }

    @Operation(summary = "Criar editora",
               description = "O CNPJ deve ser válido. Estado com 2 letras (RJ, SP, MG, RS...)")
    @PostMapping
    public ResponseEntity<EditoraResponseDTO> criar(@Valid @RequestBody EditoraRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(editoraService.criar(dto));
    }

    @Operation(summary = "Atualizar editora")
    @PutMapping("/{id}")
    public ResponseEntity<EditoraResponseDTO> atualizar(@PathVariable Long id,
                                                         @Valid @RequestBody EditoraRequestDTO dto) {
        return ResponseEntity.ok(editoraService.atualizar(id, dto));
    }

    @Operation(summary = "Deletar editora",
               description = "Não é permitido deletar uma editora com livros vinculados")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        editoraService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
