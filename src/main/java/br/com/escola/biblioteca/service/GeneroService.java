package br.com.escola.biblioteca.service;

import br.com.escola.biblioteca.dto.GeneroRequestDTO;
import br.com.escola.biblioteca.dto.GeneroResponseDTO;
import br.com.escola.biblioteca.entity.Genero;
import br.com.escola.biblioteca.exception.BusinessException;
import br.com.escola.biblioteca.repository.GeneroRepository;
import br.com.escola.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneroService {

    private final GeneroRepository generoRepository;
    private final LivroRepository livroRepository;

    public GeneroService(GeneroRepository generoRepository, LivroRepository livroRepository) {
        this.generoRepository = generoRepository;
        this.livroRepository = livroRepository;
    }

    public List<GeneroResponseDTO> listar() {
        return generoRepository.findAll().stream()
                .map(GeneroResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public GeneroResponseDTO buscarPorId(Long id) {
        return generoRepository.findById(id)
                .map(GeneroResponseDTO::fromEntity)
                .orElseThrow(() -> new BusinessException("Gênero não encontrado com id: " + id));
    }

    public GeneroResponseDTO criar(GeneroRequestDTO dto) {
        if (generoRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new BusinessException("Já existe um gênero com o nome: " + dto.nome());
        }
        if (generoRepository.existsBySiglaIgnoreCase(dto.sigla())) {
            throw new BusinessException("Já existe um gênero com a sigla: " + dto.sigla().toUpperCase());
        }
        Genero g = new Genero();
        g.setNome(dto.nome());
        g.setSigla(dto.sigla().toUpperCase());
        return GeneroResponseDTO.fromEntity(generoRepository.save(g));
    }

    public GeneroResponseDTO atualizar(Long id, GeneroRequestDTO dto) {
        Genero g = generoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Gênero não encontrado com id: " + id));

        // Verifica duplicatas excluindo o próprio registro
        generoRepository.findAll().stream()
                .filter(existing -> !existing.getId().equals(id))
                .forEach(existing -> {
                    if (existing.getNome().equalsIgnoreCase(dto.nome()))
                        throw new BusinessException("Já existe outro gênero com o nome: " + dto.nome());
                    if (existing.getSigla().equalsIgnoreCase(dto.sigla()))
                        throw new BusinessException("Já existe outro gênero com a sigla: " + dto.sigla().toUpperCase());
                });

        g.setNome(dto.nome());
        g.setSigla(dto.sigla().toUpperCase());
        return GeneroResponseDTO.fromEntity(generoRepository.save(g));
    }

    public void deletar(Long id) {
        Genero g = generoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Gênero não encontrado com id: " + id));

        if (livroRepository.existsByGeneroId(id)) {
            throw new BusinessException(
                "Não é possível excluir o gênero '" + g.getNome() + "' pois existem livros vinculados a ele. " +
                "Remova ou reatribua os livros antes de deletar o gênero."
            );
        }
        generoRepository.deleteById(id);
    }
}
