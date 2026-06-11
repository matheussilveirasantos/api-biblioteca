package br.com.escola.biblioteca.exception;

public class Validador {

    public static void validarNulo(Object valor, String mensagem) {
        if (valor == null) {
            throw new BusinessException(mensagem);
        }
    }

    public static void validarNuloOuVazio(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new BusinessException(mensagem);
        }
    }
}