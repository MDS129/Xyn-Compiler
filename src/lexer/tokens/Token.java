package lexer.tokens;

public class Token {
    public final TokenType type;

    /// for accessing the value, use `source.substring(startIdx, endIdx)`
    public final int startIdx, endIdx;
    public final int startCol, endCol, line;

    public boolean isCheckpoint = false;

    public Token(int s, int e, TokenType t, int sc, int ec, int l) {
        startIdx = s;
        endIdx = e;
        type = t;
        startCol = sc;
        endCol = ec;
        line = l;
    }


    @Override
    public String toString() {
        return String.format("{type: Token.%s, value: %d-%d col: %d-%d, line %d}", type, startIdx, endIdx, startCol, endCol, line);
    }
}