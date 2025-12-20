# Tokens

## Token:
```java
public class Token {
    public final String value;
    public final TokenType type;
    
    public int startCol, endCol, line;
    
    public Token(String v, TokenType t, int sc, int ec, int l) {
        value = v;
        type = t;
        startCol = sc;
        endCol = ec;
        line = l;
    }
    
    @Override
    public String toString() {
        return String.format("{type: Token.%s, value: \"%s\", col: %d-%d, line %d", type, value, startCol, endCol, line);
    }
}
```

## TokenType:
```java
public enum TokenType {
    Let,
    Dyn,
    
    Ident,
    
    Digit,
    
    Eq,
    Colon,
    
    Unk
}
```

# lexer.Lexer
### - lexer.Lexer 0:
```java
public final class Lexer {
    private final String src;
    public Lexer(String s) {
        src = s;
    }
    
    public void lex() {
        //TODO
    }
}
```

### - lexer.Lexer 1:

```java
package lexer;

import lexer.tokens.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Lexer {
    // string pool
    private Map<String, String> pool;

    private static final Map<String, TokenType> KEYWORDS = new HashMap<>(Map.of(
            "let", TokenType.Let,
            "dyn", TokenType.Dyn
    ));

    // for substring purpose
    private final String src;
    // for char access by index purpose
    private final char[] chars;

    private final int length;
    private int ptrIdx = 0;
    private int col = 1;
    private int line = 1;

    public Lexer(String s) {
        pool = new HashMap<>();
        src = s;
        length = s.length();
        chars = s.toCharArray();
    }

    private boolean isWhitespace(char c) {
        // newline skipped for line tracking purpose
        return c != '\n' && Character.isWhitespace(c);
    }

    private void skipWhitespace() {
        while (ptrIdx < length && isWhitespace(chars[ptrIdx])) {
            ++ptrIdx;
            ++col;
        }
    }

    private boolean isAlphanumeric(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '_');
    }

    private Token getAlpha(int startIdx, int startCol) {
        while (ptrIdx < length && isAlphanumeric(chars[ptrIdx])) {
            ++ptrIdx;
            ++col;
        }
        String w = freeze(src.substring(startIdx, ptrIdx));
        return new Token(w, KEYWORDS.getOrDefault(w, TokenType.Ident), startCol, col, line);
    }

    private Token getDigit(int startIdx, int startCol) {
        while (ptrIdx < length && chars[ptrIdx] >= '0' && chars[ptrIdx] <= '9') {
            ++ptrIdx;
            ++col;
        }
        return new Token(src.substring(startIdx, ptrIdx), TokenType.Digit, startCol, col, line);
    }

    private String freeze(String s) {
        return pool.computeIfAbsent(s, k -> k);
    }

    public void resetPool() {
        pool = new HashMap<>();
    }

    public List<Token> lex() {
        final List<Token> tokens = new ArrayList<>();
        while (ptrIdx < length) {
            skipWhitespace();
            if (ptrIdx >= length) return tokens;

            char c = chars[ptrIdx];
            switch (c) {
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
                    tokens.add(getDigit(ptrIdx, col));

                default:
                    // Digit had already checked before, so it's ok to use this method
                    if (isAlphanumeric(c)) {
                        tokens.add(getAlpha(ptrIdx, col));
                    } else {
                        tokens.add(new Token(String.valueOf(chars[ptrIdx++]), TokenType.Unk, col++, col, line));
                    }
            }

        }
    }
}
```