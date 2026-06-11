package br.com.escola.biblioteca.controller;

import br.com.escola.biblioteca.dto.AutorRequestDTO;
import br.com.escola.biblioteca.dto.AutorResponseDTO;
import br.com.escola.biblioteca.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
@Tag(name = "Autores", description = "CRUD de autores")
@SecurityRequirement(name = "BearerAuth")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @Operation(summary = "Listar autores")
    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> listar() {
        return ResponseEntity.ok(autorService.listar());
    }

    @Operation(summary = "Buscar autor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.buscarPorId(id));
    }

    @Operation(summary = "Cadastrar autor")
    @PostMapping
    public ResponseEntity<AutorResponseDTO> criar(@Valid @RequestBody AutorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.salvar(dto));
    }

    @Operation(summary = "Cadastrar autores em lote")
    @PostMapping("/lote")
    public ResponseEntity<List<AutorResponseDTO>> criarLote(@Valid @RequestBody List<AutorRequestDTO> dtos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.salvarLote(dtos));
    }

    @Operation(summary = "Atualizar autor")
    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> atualizar(@PathVariable Long id,
                                                       @Valid @RequestBody AutorRequestDTO dto) {
        return ResponseEntity.ok(autorService.atualizar(id, dto));
    }

    @Operation(summary = "Deletar autor",
               description = "Não é permitido deletar um autor com livros vinculados")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        autorService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
