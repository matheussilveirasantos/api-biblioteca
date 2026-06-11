package br.com.escola.biblioteca.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título não pode estar vazio")
    @Size(max = 200, message = "O título ultrapassou o limite de caracteres")
    @Column(nullable = false, length = 200)
    private String titulo;

    @NotBlank(message = "O ISBN não pode estar vazio")
    @Size(max = 13, message = "Você ultrapassou o numero máximo de caracteres")
    @Column(nullable = false, length = 13)
    private String isbn;

    @NotNull(message = "O ano de publicação não pode estar vazio")
    @Column(nullable = false)
    private Integer anoPublicacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_autor", nullable = false)
    private Autor autor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_editora", nullable = false)
    private Editora editora;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_genero", nullable = false)
    private Genero genero;

    public Livro() {}

    public Livro(Long id, String titulo, String isbn, Integer anoPublicacao,
                 Autor autor, Editora editora, Genero genero) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.anoPublicacao = anoPublicacao;
        this.autor = autor;
        this.editora = editora;
        this.genero = genero;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }

    public Editora getEditora() { return editora; }
    public void setEditora(Editora editora) { this.editora = editora; }

    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livro)) return false;
        Livro livro = (Livro) o;
        return id != null && id.equals(livro.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Livro{id=" + id + ", titulo='" + titulo + "', isbn='" + isbn + "'}";
    }
}
