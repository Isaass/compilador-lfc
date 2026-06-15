package br.compiladores.codegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtimizadorCodigoTresEnderecos {

    private static final Pattern ATRIBUICAO = Pattern.compile("^\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s=\\s(.+)$");
    private static final Pattern TEMPORARIO = Pattern.compile("\\bt\\d+\\b");
    private static final Pattern OPERACAO_BINARIA = Pattern.compile("^(-?\\d+)\\s*(>=|<=|==|<>|AND|OR|[+\\-*/<>])\\s*(-?\\d+)$");
    private static final Pattern OPERACAO_UNARIA_NEGATIVA = Pattern.compile("^-(-?\\d+)$");
    private static final Pattern OPERACAO_UNARIA_LOGICA = Pattern.compile("^~(-?\\d+)$");

    public String otimizar(String codigoOriginal) {
        List<String> linhas = separarLinhas(codigoOriginal);
        List<String> comConstantes = aplicarDobramentoEPropagacao(linhas);
        List<String> semCodigoMorto = eliminarTemporariosMortos(comConstantes);

        return String.join(System.lineSeparator(), semCodigoMorto) + System.lineSeparator();
    }

    private List<String> separarLinhas(String codigo) {
        String[] linhas = codigo.split("\\R");
        List<String> resultado = new ArrayList<>();

        for (String linha : linhas) {
            if (!linha.trim().isEmpty()) {
                resultado.add(linha);
            }
        }

        return resultado;
    }

    private List<String> aplicarDobramentoEPropagacao(List<String> linhas) {
        List<String> resultado = new ArrayList<>();
        Map<String, String> constantes = new HashMap<>();

        for (String linha : linhas) {
            String linhaAtual = substituirTemporariosPorConstantes(linha, constantes);

            Matcher matcher = ATRIBUICAO.matcher(linhaAtual);

            if (!matcher.matches()) {
                resultado.add(linhaAtual);
                continue;
            }

            String destino = matcher.group(1);
            String expressao = matcher.group(2).trim();

            String expressaoOtimizada = avaliarExpressaoConstante(expressao);
            String novaLinha = destino + " = " + expressaoOtimizada;

            if (ehTemporario(destino) && ehNumero(expressaoOtimizada)) {
                constantes.put(destino, expressaoOtimizada);
            } else if (ehTemporario(destino)) {
                constantes.remove(destino);
            }

            resultado.add(novaLinha);
        }

        return resultado;
    }

    private String substituirTemporariosPorConstantes(String linha, Map<String, String> constantes) {
        Matcher matcher = TEMPORARIO.matcher(linha);
        StringBuffer resultado = new StringBuffer();

        while (matcher.find()) {
            String temporario = matcher.group();

            if (constantes.containsKey(temporario)) {
                matcher.appendReplacement(resultado, constantes.get(temporario));
            } else {
                matcher.appendReplacement(resultado, temporario);
            }
        }

        matcher.appendTail(resultado);

        return resultado.toString();
    }

    private String avaliarExpressaoConstante(String expressao) {
        Matcher binaria = OPERACAO_BINARIA.matcher(expressao);

        if (binaria.matches()) {
            int esquerda = Integer.parseInt(binaria.group(1));
            String operador = binaria.group(2);
            int direita = Integer.parseInt(binaria.group(3));

            return avaliarBinaria(esquerda, operador, direita);
        }

        Matcher unariaNegativa = OPERACAO_UNARIA_NEGATIVA.matcher(expressao);

        if (unariaNegativa.matches()) {
            int valor = Integer.parseInt(unariaNegativa.group(1));
            return String.valueOf(-valor);
        }

        Matcher unariaLogica = OPERACAO_UNARIA_LOGICA.matcher(expressao);

        if (unariaLogica.matches()) {
            int valor = Integer.parseInt(unariaLogica.group(1));
            return valor == 0 ? "1" : "0";
        }

        return expressao;
    }

    private String avaliarBinaria(int esquerda, String operador, int direita) {
        switch (operador) {
            case "+":
                return String.valueOf(esquerda + direita);
            case "-":
                return String.valueOf(esquerda - direita);
            case "*":
                return String.valueOf(esquerda * direita);
            case "/":
                if (direita == 0) {
                    return esquerda + " / " + direita;
                }
                return String.valueOf(esquerda / direita);
            case ">":
                return esquerda > direita ? "1" : "0";
            case "<":
                return esquerda < direita ? "1" : "0";
            case ">=":
                return esquerda >= direita ? "1" : "0";
            case "<=":
                return esquerda <= direita ? "1" : "0";
            case "==":
                return esquerda == direita ? "1" : "0";
            case "<>":
                return esquerda != direita ? "1" : "0";
            case "AND":
                return esquerda != 0 && direita != 0 ? "1" : "0";
            case "OR":
                return esquerda != 0 || direita != 0 ? "1" : "0";
            default:
                return esquerda + " " + operador + " " + direita;
        }
    }

    private List<String> eliminarTemporariosMortos(List<String> linhas) {
        List<String> resultadoInvertido = new ArrayList<>();
        Set<String> temporariosUsados = new HashSet<>();

        for (int i = linhas.size() - 1; i >= 0; i--) {
            String linha = linhas.get(i);
            String temporarioDefinido = obterTemporarioDefinido(linha);

            if (temporarioDefinido != null && !temporariosUsados.contains(temporarioDefinido)) {
                continue;
            }

            if (temporarioDefinido != null) {
                temporariosUsados.remove(temporarioDefinido);
            }

            temporariosUsados.addAll(obterTemporariosUsados(linha));
            resultadoInvertido.add(linha);
        }

        Collections.reverse(resultadoInvertido);

        return resultadoInvertido;
    }

    private String obterTemporarioDefinido(String linha) {
        Matcher matcher = ATRIBUICAO.matcher(linha);

        if (!matcher.matches()) {
            return null;
        }

        String destino = matcher.group(1);

        if (ehTemporario(destino)) {
            return destino;
        }

        return null;
    }

    private Set<String> obterTemporariosUsados(String linha) {
        Set<String> temporarios = new HashSet<>();

        Matcher atribuicao = ATRIBUICAO.matcher(linha);
        String trechoParaBuscar = linha;

        if (atribuicao.matches()) {
            trechoParaBuscar = atribuicao.group(2);
        }

        Matcher matcher = TEMPORARIO.matcher(trechoParaBuscar);

        while (matcher.find()) {
            temporarios.add(matcher.group());
        }

        return temporarios;
    }

    private boolean ehTemporario(String valor) {
        return valor.matches("t\\d+");
    }

    private boolean ehNumero(String valor) {
        return valor.matches("-?\\d+");
    }
}