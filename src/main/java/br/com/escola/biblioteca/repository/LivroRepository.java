package br.com.escola.biblioteca.repository;

import br.com.escola.biblioteca.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByAutorId(Long autorId);
    List<Livro> findByGeneroId(Long generoId);
    List<Livro> findByEditoraId(Long editoraId);
    boolean existsByAutorId(Long autorId);
    boolean existsByGeneroId(Long generoId);
    boolean existsByEditoraId(Long editoraId);
}
