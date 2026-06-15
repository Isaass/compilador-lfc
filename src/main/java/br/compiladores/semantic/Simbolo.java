package br.compiladores.semantic;

public class Simbolo {

    private final String nome;
    private final Tipo tipo;
    private final int deslocamento;

    public Simbolo(String nome, Tipo tipo, int deslocamento) {
        this.nome = nome;
        this.tipo = tipo;
        this.deslocamento = deslocamento;
    }

    public String getNome() {
        return nome;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getDeslocamento() {
        return deslocamento;
    }
}