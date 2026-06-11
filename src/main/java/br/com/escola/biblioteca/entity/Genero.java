package br.com.escola.biblioteca.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "genero")
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do gênero é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    @NotBlank(message = "A sigla do gênero é obrigatória")
    @Size(max = 10, message = "A sigla deve ter no máximo 10 caracteres")
    @Column(nullable = false, length = 10, unique = true)
    private String sigla;

    @OneToMany(mappedBy = "genero")
    private List<Livro> livros;

    public Genero() {}

    public Genero(Long id, String nome, String sigla, List<Livro> livros) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.livros = livros;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSigla() { return sigla; }
    public void setSigla(String sigla) { this.sigla = sigla; }

    public List<Livro> getLivros() { return livros; }
    public void setLivros(List<Livro> livros) { this.livros = livros; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genero)) return false;
        Genero genero = (Genero) o;
        return id != null && id.equals(genero.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Genero{id=" + id + ", nome='" + nome + "', sigla='" + sigla + "'}";
    }
}
