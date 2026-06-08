// Generated from SimpleLang.g4 by ANTLR 4.13.2
package br.compiladores.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SimpleLangParser}.
 */
public interface SimpleLangListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#programa}.
	 * @param ctx the parse tree
	 */
	void enterPrograma(SimpleLangParser.ProgramaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#programa}.
	 * @param ctx the parse tree
	 */
	void exitPrograma(SimpleLangParser.ProgramaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#declaracoes}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracoes(SimpleLangParser.DeclaracoesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#declaracoes}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracoes(SimpleLangParser.DeclaracoesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#listaDeclaracoes}.
	 * @param ctx the parse tree
	 */
	void enterListaDeclaracoes(SimpleLangParser.ListaDeclaracoesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#listaDeclaracoes}.
	 * @param ctx the parse tree
	 */
	void exitListaDeclaracoes(SimpleLangParser.ListaDeclaracoesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#declaracao}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracao(SimpleLangParser.DeclaracaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#declaracao}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracao(SimpleLangParser.DeclaracaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#listaIds}.
	 * @param ctx the parse tree
	 */
	void enterListaIds(SimpleLangParser.ListaIdsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#listaIds}.
	 * @param ctx the parse tree
	 */
	void exitListaIds(SimpleLangParser.ListaIdsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#tipo}.
	 * @param ctx the parse tree
	 */
	void enterTipo(SimpleLangParser.TipoContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#tipo}.
	 * @param ctx the parse tree
	 */
	void exitTipo(SimpleLangParser.TipoContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#comandoComposto}.
	 * @param ctx the parse tree
	 */
	void enterComandoComposto(SimpleLangParser.ComandoCompostoContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#comandoComposto}.
	 * @param ctx the parse tree
	 */
	void exitComandoComposto(SimpleLangParser.ComandoCompostoContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#listaComandos}.
	 * @param ctx the parse tree
	 */
	void enterListaComandos(SimpleLangParser.ListaComandosContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#listaComandos}.
	 * @param ctx the parse tree
	 */
	void exitListaComandos(SimpleLangParser.ListaComandosContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#comando}.
	 * @param ctx the parse tree
	 */
	void enterComando(SimpleLangParser.ComandoContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#comando}.
	 * @param ctx the parse tree
	 */
	void exitComando(SimpleLangParser.ComandoContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#comandoIf}.
	 * @param ctx the parse tree
	 */
	void enterComandoIf(SimpleLangParser.ComandoIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#comandoIf}.
	 * @param ctx the parse tree
	 */
	void exitComandoIf(SimpleLangParser.ComandoIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#comandoWhile}.
	 * @param ctx the parse tree
	 */
	void enterComandoWhile(SimpleLangParser.ComandoWhileContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#comandoWhile}.
	 * @param ctx the parse tree
	 */
	void exitComandoWhile(SimpleLangParser.ComandoWhileContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#comandoRead}.
	 * @param ctx the parse tree
	 */
	void enterComandoRead(SimpleLangParser.ComandoReadContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#comandoRead}.
	 * @param ctx the parse tree
	 */
	void exitComandoRead(SimpleLangParser.ComandoReadContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#comandoWrite}.
	 * @param ctx the parse tree
	 */
	void enterComandoWrite(SimpleLangParser.ComandoWriteContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#comandoWrite}.
	 * @param ctx the parse tree
	 */
	void exitComandoWrite(SimpleLangParser.ComandoWriteContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#listaWrite}.
	 * @param ctx the parse tree
	 */
	void enterListaWrite(SimpleLangParser.ListaWriteContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#listaWrite}.
	 * @param ctx the parse tree
	 */
	void exitListaWrite(SimpleLangParser.ListaWriteContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#comandoAtribuicao}.
	 * @param ctx the parse tree
	 */
	void enterComandoAtribuicao(SimpleLangParser.ComandoAtribuicaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#comandoAtribuicao}.
	 * @param ctx the parse tree
	 */
	void exitComandoAtribuicao(SimpleLangParser.ComandoAtribuicaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#expressao}.
	 * @param ctx the parse tree
	 */
	void enterExpressao(SimpleLangParser.ExpressaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#expressao}.
	 * @param ctx the parse tree
	 */
	void exitExpressao(SimpleLangParser.ExpressaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#expressaoLogica}.
	 * @param ctx the parse tree
	 */
	void enterExpressaoLogica(SimpleLangParser.ExpressaoLogicaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#expressaoLogica}.
	 * @param ctx the parse tree
	 */
	void exitExpressaoLogica(SimpleLangParser.ExpressaoLogicaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#expressaoRelacional}.
	 * @param ctx the parse tree
	 */
	void enterExpressaoRelacional(SimpleLangParser.ExpressaoRelacionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#expressaoRelacional}.
	 * @param ctx the parse tree
	 */
	void exitExpressaoRelacional(SimpleLangParser.ExpressaoRelacionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#expressaoAditiva}.
	 * @param ctx the parse tree
	 */
	void enterExpressaoAditiva(SimpleLangParser.ExpressaoAditivaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#expressaoAditiva}.
	 * @param ctx the parse tree
	 */
	void exitExpressaoAditiva(SimpleLangParser.ExpressaoAditivaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#expressaoMultiplicativa}.
	 * @param ctx the parse tree
	 */
	void enterExpressaoMultiplicativa(SimpleLangParser.ExpressaoMultiplicativaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#expressaoMultiplicativa}.
	 * @param ctx the parse tree
	 */
	void exitExpressaoMultiplicativa(SimpleLangParser.ExpressaoMultiplicativaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#expressaoUnaria}.
	 * @param ctx the parse tree
	 */
	void enterExpressaoUnaria(SimpleLangParser.ExpressaoUnariaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#expressaoUnaria}.
	 * @param ctx the parse tree
	 */
	void exitExpressaoUnaria(SimpleLangParser.ExpressaoUnariaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#expressaoPrimaria}.
	 * @param ctx the parse tree
	 */
	void enterExpressaoPrimaria(SimpleLangParser.ExpressaoPrimariaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#expressaoPrimaria}.
	 * @param ctx the parse tree
	 */
	void exitExpressaoPrimaria(SimpleLangParser.ExpressaoPrimariaContext ctx);
}