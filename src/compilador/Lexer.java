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

    private String number() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && (Character.isDigit(currentChar) || currentChar == '.')) {
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
    
    private String stringLiteral() {
        StringBuilder result = new StringBuilder();
        advance(); // Skip the opening quote
        while (currentChar != '\0' && currentChar != '\'') {
            result.append(currentChar);
            advance();
        }
        advance(); // Skip the closing quote
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
                String num = number();
                if (num.contains(".")) {
                    tokens.add(new Token(TokenType.NUMFLOAT, num));
                } else {
                    tokens.add(new Token(TokenType.NUMINTEGER, num));
                }
                continue;
            }

            if (Character.isLetter(currentChar)) {
                String id = identifier();
                if (id.equals("int")) {
                    tokens.add(new Token(TokenType.INT, id));
                } else if (id.equals("string")) {
                    tokens.add(new Token(TokenType.STRING, id));
                } else if (id.equals("float")) {
                    tokens.add(new Token(TokenType.FLOAT, id));
                } else if (id.equals("char")) {
                    tokens.add(new Token(TokenType.CHAR, id));
                } else if (id.equals("print")) {
                    tokens.add(new Token(TokenType.PRINT, id));
                } else if (id.equals("loop")) {
                    tokens.add(new Token(TokenType.LOOP, id));
                } else if (id.equals("if")) {
                    tokens.add(new Token(TokenType.IF, id));
                } else if (id.equals("else")) {
                    tokens.add(new Token(TokenType.ELSE, id));
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
            
            if (currentChar == '(') {
                tokens.add(new Token(TokenType.OPENPARENTESE, "("));
                advance();
                continue;
            }
            
            if (currentChar == ')') {
                tokens.add(new Token(TokenType.CLOSEPARENTESE, ")"));
                advance();
                continue;
            }
            
            if (currentChar == '{') {
                tokens.add(new Token(TokenType.OPENBRACE, "{"));
                advance();
                continue;
            }

            if (currentChar == '}') {
                tokens.add(new Token(TokenType.CLOSEBRACE, "}"));
                advance();
                continue;
            }
            
            if (currentChar == '>') {
                tokens.add(new Token(TokenType.MAIOR, ">"));
                advance();
                continue;
            }

            if (currentChar == '<') {
                tokens.add(new Token(TokenType.MENOR, "<"));
                advance();
                continue;
            }

            if (currentChar == ';') {
                tokens.add(new Token(TokenType.SEMICOLON, ";"));
                advance();
                continue;
            }
            
            if (currentChar == '+') {
                tokens.add(new Token(TokenType.INCREMENT, ";"));
                advance();
                continue;
            }

            if (currentChar == '\'') {
                tokens.add(new Token(TokenType.TEXTSTRING, stringLiteral()));
                continue;
            }

            throw new RuntimeException("Caractere inesperado: " + currentChar);
        }

        return tokens;
    }
}
