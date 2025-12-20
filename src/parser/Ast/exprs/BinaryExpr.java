package parser.Ast.exprs;

import lexer.tokens.TokenType;
import parser.Ast.AstNode;

public final class BinaryExpr extends Expr {
    public final AstNode left;
    public final TokenType op;
    public final AstNode right;

    public BinaryExpr(AstNode l, TokenType o, AstNode r, int si, int ei, int sc, int ec, int ln) {
        super(si, ei, sc, ec, ln);
        left = l;
        op = o;
        right = r;
    }
}
