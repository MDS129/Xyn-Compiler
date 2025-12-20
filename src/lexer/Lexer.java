package lexer;

import errors.ErrorEngine;
import lexer.tokens.Token;
import lexer.tokens.TokenType;
import org.jetbrains.annotations.NotNull;


public final class Lexer {
    /// For substring
    private final String src;
    /// For char indexing
    private final char[] chars;
    private final int srcLen;


    public int idx;
    private int col;
    private int line;
    public Lexer(@NotNull String s) {
        if (s.isEmpty()) {
            IO.println("Error: Source must not be empty");
            System.exit(0);
        }
        ErrorEngine.source = s;
        src = s;
        chars = s.toCharArray();
        srcLen = s.length();
        idx = 0;
        col = 1;
        line = 1;

    }


    private TokenType KeywordsOrDefault(int startIdx) {
        // i googled it and it says that this method don't need a new string
        if (src.regionMatches(startIdx, "let", 0, 3)) {
            return TokenType.Let;
        }
        return TokenType.Ident;
    }

    private void skipWhitespace() {
        // another loop
        while (idx < srcLen && chars[idx] != '\n' && Character.isWhitespace(chars[idx])) {
            ++idx;
            ++col;
        }
    }

    private Token getDigit(int startIdx, int startCol) {
        // another loop
        while (idx < srcLen && chars[idx] >= '0' && chars[idx] <= '9') {
            ++idx;
            ++col;
        }
        if (chars[idx] == '.') {
            ++idx;
            ++col;
            if (!(chars[idx] >= '0' && chars[idx] <= '9')) {
                --idx;
                --col;
                return new Token(startIdx, idx-1, TokenType.Digit, startCol, col-1, line);
            }
            while (idx < srcLen && chars[idx] >= '0' && chars[idx] <= '9') {
                ++idx;
                ++col;
            }
            return new Token(startIdx, idx, TokenType.Float, startCol, col, line);
        }
        return new Token(startIdx, idx, TokenType.Digit, startCol, col, line);
    }

    private boolean isIdent(char c) {
        return  (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_';
    }
    private Token getIdent(int startIdx, int startCol) {
        // yet another loop
        while (idx < srcLen && isIdent(chars[idx])) {
            ++idx;
            ++col;
        }
        // the token stores the startIdx and endIdx, because String is expensive in Java. So i will make the string only if i really need it (late materialization)
        return new Token(startIdx, idx, KeywordsOrDefault(startIdx), startCol, col, line);
    }

    private Token getString(int startIdx, int startCol) {
        while (idx < srcLen && chars[idx] != '"') {
            ++idx;
            ++col;
        }
        if (idx >= srcLen) {
            ErrorEngine.addWithValue("Syntax Error: Unterminated string value `%s`.",
                    line, startIdx, idx,
                    ErrorEngine.source.substring(startIdx+1, idx)
            );
        }
        return new Token(startIdx, (idx >= srcLen) ? idx : ++idx, TokenType.String, startCol, (idx >= srcLen) ? col : ++col, line);
    }
    public Token[] lex() {
        //* - Token:
        //* f1: startIdx(int)
        //* f2: endIdx(int)
        //* f3: type(TokenType)
        //* f4: startCol(int)
        //* f5: endCol(int)
        //* f6: line(int)

        final Token[] tokens = new Token[Math.max(8, srcLen/2)];
        int elm = 0;
        // the main loop
        while (idx < srcLen) {
            skipWhitespace();
            if (idx >= srcLen) break;

            char c = chars[idx];
            switch(c) {
                case '\n':
                    ++line; ++idx;
                    col = 1;
                    break;

                case '=':
                    tokens[elm++] = new Token(idx, idx++, TokenType.Eq, col++, col, line);
                    break;

                case '+':
                    tokens[elm++] = new Token(idx, idx++, TokenType.Plus, col++, col, line);
                    break;

                case '-':
                    tokens[elm++] = new Token(idx, idx++, TokenType.Minus, col++, col, line);
                    break;

                case '*':
                    tokens[elm++] = new Token(idx, idx++, TokenType.Mul, col++, col, line);
                    break;

                case '/':
                    tokens[elm++] = new Token(idx, idx++, TokenType.Div, col++, col, line);
                    break;

                case ':':
                    tokens[elm++] = new Token(idx, idx++, TokenType.Colon, col++, col, line);
                    break;

                case ';':
                    tokens[elm++] = new Token(idx, idx++, TokenType.Semicolon, col++, col, line);
                    break;

                case '"':
                    tokens[elm++] = getString(idx++, col++);
                    break;


                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    tokens[elm++] = getDigit(idx++, col++);
                    break;

                default:
                    // digit is already checked, so u can use isIdent method
                    tokens[elm++] =
                            isIdent(c) ?
                                    getIdent(idx, col) :
                                    new Token(idx, idx++, TokenType.Unk, col++, col, line)
                    ;
                    break;
            }
        }
        idx = elm;
        return tokens;
    }

}