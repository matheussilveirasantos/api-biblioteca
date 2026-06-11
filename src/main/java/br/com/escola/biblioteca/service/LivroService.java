package br.com.escola.biblioteca.service;

import br.com.escola.biblioteca.dto.LivroRequestDTO;
import br.com.escola.biblioteca.dto.LivroResponseDTO;
import br.com.escola.biblioteca.entity.Autor;
import br.com.escola.biblioteca.entity.Editora;
import br.com.escola.biblioteca.entity.Genero;
import br.com.escola.biblioteca.entity.Livro;
import br.com.escola.biblioteca.exception.BusinessException;
import br.com.escola.biblioteca.repository.AutorRepository;
import br.com.escola.biblioteca.repository.EditoraRepository;
import br.com.escola.biblioteca.repository.GeneroRepository;
import br.com.escola.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final GeneroRepository generoRepository;
    private final EditoraRepository editoraRepository;
    private final EmailService emailService;

    public LivroService(LivroRepository livroRepository,
                        AutorRepository autorRepository,
                        GeneroRepository generoRepository,
                        EditoraRepository editoraRepository,
                        EmailService emailService) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.generoRepository = generoRepository;
        this.editoraRepository = editoraRepository;
        this.emailService = emailService;
    }

    public LivroResponseDTO criar(LivroRequestDTO dto) {
        Livro livro = montarLivro(new Livro(), dto);
        Livro salvo = livroRepository.save(livro);
        emailService.enviarEmailCadastroLivro(salvo);
        return LivroResponseDTO.fromEntity(salvo);
    }

    public List<LivroResponseDTO> criarLote(List<LivroRequestDTO> dtos) {
        List<Livro> livros = dtos.stream()
                .map(dto -> montarLivro(new Livro(), dto))
                .collect(Collectors.toList());
        List<Livro> salvos = livroRepository.saveAll(livros);
        salvos.forEach(emailService::enviarEmailCadastroLivro);
        return salvos.stream().map(LivroResponseDTO::fromEntity).collect(Collectors.toList());
    }

    public List<LivroResponseDTO> listarTodos() {
        return livroRepository.findAll().stream()
                .map(LivroResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public LivroResponseDTO buscarPorId(Long id) {
        return livroRepository.findById(id)
                .map(LivroResponseDTO::fromEntity)
                .orElseThrow(() -> new BusinessException("Livro não encontrado com id: " + id));
    }

    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Livro não encontrado com id: " + id));
        montarLivro(livro, dto);
        Livro salvo = livroRepository.save(livro);
        emailService.enviarEmailAlteracaoLivro(salvo);
        return LivroResponseDTO.fromEntity(salvo);
    }

    public void deletar(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Livro não encontrado com id: " + id));
        livroRepository.deleteById(id);
        emailService.enviarEmailExclusaoLivro(livro);
    }

    private Livro montarLivro(Livro livro, LivroRequestDTO dto) {
        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new BusinessException("Autor não encontrado com id: " + dto.autorId()));
        Genero genero = generoRepository.findById(dto.generoId())
                .orElseThrow(() -> new BusinessException("Gênero não encontrado com id: " + dto.generoId()));
        Editora editora = editoraRepository.findById(dto.editoraId())
                .orElseThrow(() -> new BusinessException("Editora não encontrada com id: " + dto.editoraId()));

        livro.setTitulo(dto.titulo());
        livro.setIsbn(dto.isbn());
        livro.setAnoPublicacao(dto.anoPublicacao());
        livro.setAutor(autor);
        livro.setGenero(genero);
        livro.setEditora(editora);
        return livro;
    }
}
