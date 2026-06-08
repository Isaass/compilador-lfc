grammar SimpleLang;

options {
    caseInsensitive = true;
}

programa
    : PROGRAM ID PVIG declaracoes? comandoComposto PONTO EOF
    ;

declaracoes
    : VAR listaDeclaracoes
    ;

listaDeclaracoes
    : declaracao+
    ;

declaracao
    : listaIds DPONTOS tipo PVIG
    ;

listaIds
    : ID (VIG ID)*
    ;

tipo
    : INTEGER
    | BOOLEAN
    | STRING
    ;

comandoComposto
    : BEGIN listaComandos? END
    ;

listaComandos
    : comando (PVIG comando)* PVIG?
    ;

comando
    : comandoIf
    | comandoWhile
    | comandoRead
    | comandoWrite
    | comandoAtribuicao
    | comandoComposto
    ;

comandoIf
    : IF expressao THEN comando (ELSE comando)?
    ;

comandoWhile
    : WHILE expressao DO comando
    ;

comandoRead
    : READ ABPAR listaIds FPAR
    ;

comandoWrite
    : WRITE ABPAR listaWrite FPAR
    ;

listaWrite
    : expressao (VIG expressao)*
    ;

comandoAtribuicao
    : ID ATRIB expressao
    ;

expressao
    : expressaoLogica
    ;

expressaoLogica
    : expressaoRelacional (OPLOG expressaoRelacional)*
    ;

expressaoRelacional
    : expressaoAditiva (OPREL expressaoAditiva)?
    ;

expressaoAditiva
    : expressaoMultiplicativa (OPAD expressaoMultiplicativa)*
    ;

expressaoMultiplicativa
    : expressaoUnaria (OPMULT expressaoUnaria)*
    ;

expressaoUnaria
    : OPNEG expressaoUnaria
    | OPAD expressaoUnaria
    | expressaoPrimaria
    ;

expressaoPrimaria
    : ID
    | CTE
    | CADEIA
    | TRUE
    | FALSE
    | ABPAR expressao FPAR
    ;

PROGRAM : 'PROGRAM';
INTEGER : 'INTEGER';
BOOLEAN : 'BOOLEAN';
BEGIN   : 'BEGIN';
END     : 'END';
WHILE   : 'WHILE';
DO      : 'DO';
READ    : 'READ';
VAR     : 'VAR';
FALSE   : 'FALSE';
TRUE    : 'TRUE';
WRITE   : 'WRITE';
IF      : 'IF';
THEN    : 'THEN';
ELSE    : 'ELSE';
STRING  : 'STRING';

OPLOG
    : 'OR'
    | 'AND'
    ;

OPNEG
    : '~'
    ;

OPREL
    : '>='
    | '<='
    | '=='
    | '<>'
    | '>'
    | '<'
    ;

OPAD
    : '+'
    | '-'
    ;

OPMULT
    : '*'
    | '/'
    ;

ATRIB   : ':=';
PVIG    : ';';
PONTO   : '.';
DPONTOS : ':';
VIG     : ',';
ABPAR   : '(';
FPAR    : ')';

CADEIA
    : '"' (~["\r\n])* '"'
    ;

CTE
    : [0-9]+
      {
          try {
              long valor = Long.parseLong(getText());

              if (valor > 65535) {
                  throw new RuntimeException(
                      "Erro léxico na linha " + getLine() +
                      ", coluna " + getCharPositionInLine() +
                      ": constante inteira ultrapassa 2 bytes: " + getText()
                  );
              }
          } catch (NumberFormatException e) {
              throw new RuntimeException(
                  "Erro léxico na linha " + getLine() +
                  ", coluna " + getCharPositionInLine() +
                  ": constante inteira inválida: " + getText()
              );
          }
      }
    ;

ID
    : [a-z] [a-z0-9]*
      {
          if (getText().length() > 16) {
              setText(getText().substring(0, 16));
          }
      }
    ;

COMENTARIO
    : '/' ~[/\r\n]* '/' -> skip
    ;

WS
    : [ \t\r\n]+ -> skip
    ;