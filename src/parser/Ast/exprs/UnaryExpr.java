package parser.Ast.exprs;


import builtins.BuiltinType;
import parser.Ast.AstNode;

public final class UnaryExpr extends Expr {
    public final AstNode expr;

    public UnaryExpr(AstNode e, int si, int ei, int sc, int ec, int ln) {
        super(si, ei, sc, ec, ln);
        expr = e;
        type = BuiltinType.Integer;
    }
}
