package br.compiladores;

import br.compiladores.parser.SimpleLangLexer;
import br.compiladores.parser.SimpleLangParser;
import br.compiladores.semantic.AnalisadorSemantico;
import br.compiladores.codegen.GeradorCodigo;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

public class Main {

    public static void main(String[] args) throws Exception {
        String caminhoArquivo = args.length > 0 ? args[0] : "exemplos/valido.txt";

        CharStream input = CharStreams.fromFileName(caminhoArquivo);

        SimpleLangLexer lexer = new SimpleLangLexer(input);

        lexer.removeErrorListeners();
        lexer.addErrorListener(new ErroLexicoListener());

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        try {
            tokens.fill();

            imprimirTokens(tokens);

            SimpleLangParser parser = new SimpleLangParser(tokens);

            parser.removeErrorListeners();
            parser.addErrorListener(new ErroSintaticoListener());

            ParseTree arvore = parser.programa();

            System.out.println("Análise sintática finalizada com sucesso!");

            AnalisadorSemantico semantico = new AnalisadorSemantico();
            semantico.visit(arvore);

            System.out.println("Análise semântica finalizada com sucesso!");

            semantico.getTabela().imprimir();

            GeradorCodigo gerador = new GeradorCodigo();
            gerador.visit(arvore);

            System.out.println("CÓDIGO INTERMEDIÁRIO GERADO:");
            System.out.println("----------------------------------");
            System.out.println(gerador.getCodigo());
            System.out.println("----------------------------------");

        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void imprimirTokens(CommonTokenStream tokens) {
        System.out.println("TOKENS IDENTIFICADOS:");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-20s %-15s %-20s%n", "LEXEMA", "TIPO", "ATRIBUTO");
        System.out.println("------------------------------------------------------------");

        for (Token token : tokens.getTokens()) {
            if (token.getType() == Token.EOF) {
                continue;
            }

            String lexema = token.getText();
            String tipo = SimpleLangLexer.VOCABULARY.getSymbolicName(token.getType());
            String atributo = obterAtributo(token);

            System.out.printf("%-20s %-15s %-20s%n", lexema, tipo, atributo);
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("Análise léxica finalizada com sucesso!");
    }

    private static String obterAtributo(Token token) {
        String lexema = token.getText();

        switch (token.getType()) {
            case SimpleLangLexer.OPAD:
                if (lexema.equals("+")) return "MAIS";
                if (lexema.equals("-")) return "MENOS";
                break;

            case SimpleLangLexer.OPMULT:
                if (lexema.equals("*")) return "VEZES";
                if (lexema.equals("/")) return "DIV";
                break;

            case SimpleLangLexer.OPREL:
                if (lexema.equals("<")) return "MENOR";
                if (lexema.equals("<=")) return "MENIG";
                if (lexema.equals(">")) return "MAIOR";
                if (lexema.equals(">=")) return "MAIG";
                if (lexema.equals("==")) return "IGUAL";
                if (lexema.equals("<>")) return "DIFER";
                break;

            case SimpleLangLexer.OPLOG:
                if (lexema.equalsIgnoreCase("OR")) return "OR";
                if (lexema.equalsIgnoreCase("AND")) return "AND";
                break;

            case SimpleLangLexer.OPNEG:
                return "NEG";

            case SimpleLangLexer.ID:
            case SimpleLangLexer.CTE:
            case SimpleLangLexer.CADEIA:
                return lexema;

            default:
                return "-";
        }

        return "-";
    }

    private static class ErroLexicoListener extends BaseErrorListener {
        @Override
        public void syntaxError(
                Recognizer<?, ?> recognizer,
                Object offendingSymbol,
                int line,
                int charPositionInLine,
                String msg,
                RecognitionException e
        ) {
            throw new RuntimeException(
                    "Erro léxico na linha " + line +
                            ", coluna " + charPositionInLine +
                            ": " + msg
            );
        }
    }

    private static class ErroSintaticoListener extends BaseErrorListener {
        @Override
        public void syntaxError(
                Recognizer<?, ?> recognizer,
                Object offendingSymbol,
                int line,
                int charPositionInLine,
                String msg,
                RecognitionException e
        ) {
            throw new RuntimeException(
                    "Erro sintático na linha " + line +
                            ", coluna " + charPositionInLine +
                            ": " + msg
            );
        }
    }
}