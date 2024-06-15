package compilador;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int pos;
    private Token currentToken;
    private SymbolTable symbolTable;

    public Parser(List<Token> tokens, SymbolTable symbolTable) {
        this.tokens = tokens;
        this.pos = 0;
        this.currentToken = tokens.get(pos);
        this.symbolTable = symbolTable;
    }

    private void advance() {
        pos++;
        if (pos < tokens.size()) {
            currentToken = tokens.get(pos);
        } else {
            currentToken = null;
        }
    }

    private void eat(TokenType type) {
        if (currentToken.type == type) {
            advance();
        } else {
            throw new RuntimeException("Erro de sintaxe: esperado " + type + " mas encontrado " + currentToken.type);
        }
    }

    public void parse() {
        while (currentToken != null) {
            if (currentToken.type == TokenType.INT) {
                eat(TokenType.INT);
                String id = currentToken.value;
                eat(TokenType.IDENTIFIER);
                eat(TokenType.ASSIGN);
                int value = Integer.parseInt(currentToken.value);
                eat(TokenType.INTEGER);
                eat(TokenType.SEMICOLON);
                symbolTable.add(id, value);
                System.out.println("int " + id + " = " + value + ";");
            } else if (currentToken.type == TokenType.PRINT) {
                eat(TokenType.PRINT);
                String id = currentToken.value;
                eat(TokenType.IDENTIFIER);
                eat(TokenType.SEMICOLON);
                Integer value = symbolTable.get(id);
                if (value == null) {
                    throw new RuntimeException("Variável não declarada: " + id);
                }
                System.out.println("System.out.println(" + id + ");");
            } else {
                throw new RuntimeException("Comando desconhecido: " + currentToken.type);
            }
        }
    }
}