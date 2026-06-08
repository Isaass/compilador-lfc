package br.compiladores.semantic;

import java.util.HashMap;
import java.util.Map;

public class TabelaSimbolos {

    private final Map<String, Simbolo> simbolos = new HashMap<>();

    public void declarar(String nome, Tipo tipo) {
        String chave = nome.toLowerCase();

        if (simbolos.containsKey(chave)) {
            throw new RuntimeException("Erro semântico: variável '" + nome + "' já foi declarada.");
        }

        simbolos.put(chave, new Simbolo(nome, tipo));
    }

    public Simbolo buscar(String nome) {
        String chave = nome.toLowerCase();

        Simbolo simbolo = simbolos.get(chave);

        if (simbolo == null) {
            throw new RuntimeException("Erro semântico: variável '" + nome + "' não foi declarada.");
        }

        return simbolo;
    }

    public boolean existe(String nome) {
        return simbolos.containsKey(nome.toLowerCase());
    }

    public void imprimir() {
        System.out.println("TABELA DE SÍMBOLOS:");
        System.out.println("----------------------------------");
        System.out.printf("%-20s %-10s%n", "VARIÁVEL", "TIPO");
        System.out.println("----------------------------------");

        for (Simbolo simbolo : simbolos.values()) {
            System.out.printf("%-20s %-10s%n", simbolo.getNome(), simbolo.getTipo());
        }

        System.out.println("----------------------------------");
    }
}