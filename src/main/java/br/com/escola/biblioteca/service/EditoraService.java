package br.com.escola.biblioteca.service;

import br.com.escola.biblioteca.dto.EditoraRequestDTO;
import br.com.escola.biblioteca.dto.EditoraResponseDTO;
import br.com.escola.biblioteca.entity.Editora;
import br.com.escola.biblioteca.exception.BusinessException;
import br.com.escola.biblioteca.repository.EditoraRepository;
import br.com.escola.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EditoraService {

    private final EditoraRepository editoraRepository;
    private final LivroRepository livroRepository;

    public EditoraService(EditoraRepository editoraRepository, LivroRepository livroRepository) {
        this.editoraRepository = editoraRepository;
        this.livroRepository = livroRepository;
    }

    public List<EditoraResponseDTO> listar() {
        return editoraRepository.findAll().stream()
                .map(EditoraResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public EditoraResponseDTO buscarPorId(Long id) {
        return editoraRepository.findById(id)
                .map(EditoraResponseDTO::fromEntity)
                .orElseThrow(() -> new BusinessException("Editora não encontrada com id: " + id));
    }

    public EditoraResponseDTO criar(EditoraRequestDTO dto) {
        String cnpjNormalizado = dto.cnpj().replaceAll("[^0-9]", "");

        if (editoraRepository.existsByCnpj(cnpjNormalizado)) {
            throw new BusinessException("Já existe uma editora com o CNPJ: " + dto.cnpj());
        }
        if (editoraRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new BusinessException("Já existe uma editora com o nome: " + dto.nome());
        }

        Editora e = new Editora();
        e.setNome(dto.nome());
        e.setCnpj(cnpjNormalizado);
        e.setEstado(dto.estado().toUpperCase());
        return EditoraResponseDTO.fromEntity(editoraRepository.save(e));
    }

    public EditoraResponseDTO atualizar(Long id, EditoraRequestDTO dto) {
        Editora e = editoraRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Editora não encontrada com id: " + id));

        String cnpjNormalizado = dto.cnpj().replaceAll("[^0-9]", "");

        editoraRepository.findAll().stream()
                .filter(ex -> !ex.getId().equals(id))
                .forEach(ex -> {
                    if (ex.getCnpj().equals(cnpjNormalizado))
                        throw new BusinessException("Já existe outra editora com o CNPJ: " + dto.cnpj());
                    if (ex.getNome().equalsIgnoreCase(dto.nome()))
                        throw new BusinessException("Já existe outra editora com o nome: " + dto.nome());
                });

        e.setNome(dto.nome());
        e.setCnpj(cnpjNormalizado);
        e.setEstado(dto.estado().toUpperCase());
        return EditoraResponseDTO.fromEntity(editoraRepository.save(e));
    }

    public void deletar(Long id) {
        Editora e = editoraRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Editora não encontrada com id: " + id));

        if (livroRepository.existsByEditoraId(id)) {
            throw new BusinessException(
                "Não é possível excluir a editora '" + e.getNome() + "' pois existem livros vinculados a ela. " +
                "Remova ou reatribua os livros antes de deletar a editora."
            );
        }
        editoraRepository.deleteById(id);
    }
}
