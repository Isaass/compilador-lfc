package br.compiladores.semantic;

public enum Tipo {
    INTEGER(2),
    BOOLEAN(1),
    STRING(256),
    INVALIDO(0);

    private final int tamanhoBytes;

    Tipo(int tamanhoBytes) {
        this.tamanhoBytes = tamanhoBytes;
    }

    public int getTamanhoBytes() {
        return tamanhoBytes;
    }
}