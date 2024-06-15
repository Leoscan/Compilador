package compilador;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String input;
    private int pos;
    private char currentChar;

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
        this.currentChar = input.charAt(pos);
    }

    private void advance() {
        pos++;
        if (pos >= input.length()) {
            currentChar = '\0'; // EOF
        } else {
            currentChar = input.charAt(pos);
        }
    }

    private void skipWhitespace() {
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    private String integer() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && Character.isDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        return result.toString();
    }

    private String identifier() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && (Character.isLetter(currentChar) || Character.isDigit(currentChar))) {
            result.append(currentChar);
            advance();
        }
        return result.toString();
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (currentChar != '\0') {
            if (Character.isWhitespace(currentChar)) {
                skipWhitespace();
                continue;
            }

            if (Character.isDigit(currentChar)) {
                tokens.add(new Token(TokenType.INTEGER, integer()));
                continue;
            }

            if (Character.isLetter(currentChar)) {
                String id = identifier();
                if (id.equals("int")) {
                    tokens.add(new Token(TokenType.INT, id));
                } else if (id.equals("print")) {
                    tokens.add(new Token(TokenType.PRINT, id));
                } else {
                    tokens.add(new Token(TokenType.IDENTIFIER, id));
                }
                continue;
            }

            if (currentChar == '=') {
                tokens.add(new Token(TokenType.ASSIGN, "="));
                advance();
                continue;
            }

            if (currentChar == ';') {
                tokens.add(new Token(TokenType.SEMICOLON, ";"));
                advance();
                continue;
            }

            throw new RuntimeException("Caractere inesperado: " + currentChar);
        }

        return tokens;
    }
}
