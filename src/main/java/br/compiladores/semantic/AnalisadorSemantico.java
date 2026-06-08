package br.compiladores.semantic;

import br.compiladores.parser.SimpleLangBaseVisitor;
import br.compiladores.parser.SimpleLangParser;

public class AnalisadorSemantico extends SimpleLangBaseVisitor<Tipo> {

    private final TabelaSimbolos tabela = new TabelaSimbolos();

    public TabelaSimbolos getTabela() {
        return tabela;
    }

    @Override
    public Tipo visitDeclaracao(SimpleLangParser.DeclaracaoContext ctx) {
        Tipo tipo = obterTipo(ctx.tipo());

        for (var tokenId : ctx.listaIds().ID()) {
            String nome = tokenId.getText();
            tabela.declarar(nome, tipo);
        }

        return null;
    }

    @Override
    public Tipo visitComandoAtribuicao(SimpleLangParser.ComandoAtribuicaoContext ctx) {
        String nomeVariavel = ctx.ID().getText();

        Tipo tipoVariavel = tabela.buscar(nomeVariavel).getTipo();
        Tipo tipoExpressao = visit(ctx.expressao());

        if (tipoVariavel != tipoExpressao) {
            throw new RuntimeException(
                    "Erro semântico: variável '" + nomeVariavel +
                            "' é do tipo " + tipoVariavel +
                            ", mas recebeu expressão do tipo " + tipoExpressao + "."
            );
        }

        return null;
    }

    @Override
    public Tipo visitComandoRead(SimpleLangParser.ComandoReadContext ctx) {
        for (var tokenId : ctx.listaIds().ID()) {
            String nome = tokenId.getText();
            tabela.buscar(nome);
        }

        return null;
    }

    @Override
    public Tipo visitComandoIf(SimpleLangParser.ComandoIfContext ctx) {
        Tipo tipoCondicao = visit(ctx.expressao());

        if (tipoCondicao != Tipo.BOOLEAN) {
            throw new RuntimeException("Erro semântico: condição do IF deve ser BOOLEAN.");
        }

        visit(ctx.comando(0));

        if (ctx.comando().size() > 1) {
            visit(ctx.comando(1));
        }

        return null;
    }

    @Override
    public Tipo visitComandoWhile(SimpleLangParser.ComandoWhileContext ctx) {
        Tipo tipoCondicao = visit(ctx.expressao());

        if (tipoCondicao != Tipo.BOOLEAN) {
            throw new RuntimeException("Erro semântico: condição do WHILE deve ser BOOLEAN.");
        }

        visit(ctx.comando());

        return null;
    }

    @Override
    public Tipo visitExpressaoLogica(SimpleLangParser.ExpressaoLogicaContext ctx) {
        Tipo tipoAtual = visit(ctx.expressaoRelacional(0));

        for (int i = 1; i < ctx.expressaoRelacional().size(); i++) {
            Tipo proximoTipo = visit(ctx.expressaoRelacional(i));

            if (tipoAtual != Tipo.BOOLEAN || proximoTipo != Tipo.BOOLEAN) {
                throw new RuntimeException("Erro semântico: operador lógico exige operandos BOOLEAN.");
            }

            tipoAtual = Tipo.BOOLEAN;
        }

        return tipoAtual;
    }

    @Override
    public Tipo visitExpressaoRelacional(SimpleLangParser.ExpressaoRelacionalContext ctx) {
        Tipo tipoEsquerda = visit(ctx.expressaoAditiva(0));

        if (ctx.OPREL() == null) {
            return tipoEsquerda;
        }

        Tipo tipoDireita = visit(ctx.expressaoAditiva(1));

        if (tipoEsquerda != tipoDireita) {
            throw new RuntimeException("Erro semântico: comparação entre tipos incompatíveis.");
        }

        return Tipo.BOOLEAN;
    }

    @Override
    public Tipo visitExpressaoAditiva(SimpleLangParser.ExpressaoAditivaContext ctx) {
        Tipo tipoAtual = visit(ctx.expressaoMultiplicativa(0));

        for (int i = 1; i < ctx.expressaoMultiplicativa().size(); i++) {
            Tipo proximoTipo = visit(ctx.expressaoMultiplicativa(i));

            if (tipoAtual != Tipo.INTEGER || proximoTipo != Tipo.INTEGER) {
                throw new RuntimeException("Erro semântico: operador de soma/subtração exige INTEGER.");
            }

            tipoAtual = Tipo.INTEGER;
        }

        return tipoAtual;
    }

    @Override
    public Tipo visitExpressaoMultiplicativa(SimpleLangParser.ExpressaoMultiplicativaContext ctx) {
        Tipo tipoAtual = visit(ctx.expressaoUnaria(0));

        for (int i = 1; i < ctx.expressaoUnaria().size(); i++) {
            Tipo proximoTipo = visit(ctx.expressaoUnaria(i));

            if (tipoAtual != Tipo.INTEGER || proximoTipo != Tipo.INTEGER) {
                throw new RuntimeException("Erro semântico: operador de multiplicação/divisão exige INTEGER.");
            }

            tipoAtual = Tipo.INTEGER;
        }

        return tipoAtual;
    }

    @Override
    public Tipo visitExpressaoUnaria(SimpleLangParser.ExpressaoUnariaContext ctx) {
        if (ctx.OPNEG() != null) {
            Tipo tipo = visit(ctx.expressaoUnaria());

            if (tipo != Tipo.BOOLEAN) {
                throw new RuntimeException("Erro semântico: operador de negação '~' exige BOOLEAN.");
            }

            return Tipo.BOOLEAN;
        }

        if (ctx.OPAD() != null) {
            Tipo tipo = visit(ctx.expressaoUnaria());

            if (tipo != Tipo.INTEGER) {
                throw new RuntimeException("Erro semântico: sinal '+' ou '-' exige INTEGER.");
            }

            return Tipo.INTEGER;
        }

        return visit(ctx.expressaoPrimaria());
    }

    @Override
    public Tipo visitExpressaoPrimaria(SimpleLangParser.ExpressaoPrimariaContext ctx) {
        if (ctx.ID() != null) {
            String nome = ctx.ID().getText();
            return tabela.buscar(nome).getTipo();
        }

        if (ctx.CTE() != null) {
            return Tipo.INTEGER;
        }

        if (ctx.CADEIA() != null) {
            return Tipo.STRING;
        }

        if (ctx.TRUE() != null || ctx.FALSE() != null) {
            return Tipo.BOOLEAN;
        }

        if (ctx.expressao() != null) {
            return visit(ctx.expressao());
        }

        return Tipo.INVALIDO;
    }

    private Tipo obterTipo(SimpleLangParser.TipoContext ctx) {
        if (ctx.INTEGER() != null) {
            return Tipo.INTEGER;
        }

        if (ctx.BOOLEAN() != null) {
            return Tipo.BOOLEAN;
        }

        if (ctx.STRING() != null) {
            return Tipo.STRING;
        }

        return Tipo.INVALIDO;
    }
}