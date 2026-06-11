package br.com.escola.biblioteca.service;

import br.com.escola.biblioteca.dto.AutorRequestDTO;
import br.com.escola.biblioteca.dto.AutorResponseDTO;
import br.com.escola.biblioteca.entity.Autor;
import br.com.escola.biblioteca.exception.BusinessException;
import br.com.escola.biblioteca.repository.AutorRepository;
import br.com.escola.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;

    public AutorService(AutorRepository autorRepository, LivroRepository livroRepository) {
        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
    }

    public List<AutorResponseDTO> listar() {
        return autorRepository.findAll().stream()
                .map(AutorResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public AutorResponseDTO buscarPorId(Long id) {
        return autorRepository.findById(id)
                .map(AutorResponseDTO::fromEntity)
                .orElseThrow(() -> new BusinessException("Autor não encontrado com id: " + id));
    }

    public AutorResponseDTO salvar(AutorRequestDTO dto) {
        Autor autor = new Autor();
        autor.setNome(dto.nome());
        autor.setNacionalidade(dto.nacionalidade());
        autor.setDataNascimento(dto.dataNascimento());
        return AutorResponseDTO.fromEntity(autorRepository.save(autor));
    }

    public List<AutorResponseDTO> salvarLote(List<AutorRequestDTO> dtos) {
        List<Autor> autores = dtos.stream().map(dto -> {
            Autor a = new Autor();
            a.setNome(dto.nome());
            a.setNacionalidade(dto.nacionalidade());
            a.setDataNascimento(dto.dataNascimento());
            return a;
        }).collect(Collectors.toList());
        return autorRepository.saveAll(autores).stream()
                .map(AutorResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dto) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Autor não encontrado com id: " + id));
        autor.setNome(dto.nome());
        autor.setNacionalidade(dto.nacionalidade());
        autor.setDataNascimento(dto.dataNascimento());
        return AutorResponseDTO.fromEntity(autorRepository.save(autor));
    }

    public void remover(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Autor não encontrado com id: " + id));

        if (livroRepository.existsByAutorId(id)) {
            throw new BusinessException(
                "Não é possível excluir o autor '" + autor.getNome() + "' pois existem livros vinculados a ele. " +
                "Remova os livros do autor antes de excluí-lo."
            );
        }
        autorRepository.deleteById(id);
    }
}
