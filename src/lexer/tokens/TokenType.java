package lexer.tokens;

public enum TokenType {
    Let,
    Dyn,

    Ident,

    Digit,
    Float,
    String,

    Plus,
    Minus,
    Mul,
    Div,

    Lparen,
    Rparen,

    /// is a special member for context only
    Operation,

    Eq,
    Semicolon,
    Colon,

    Unk
}
