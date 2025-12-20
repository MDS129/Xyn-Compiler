package parser.Ast.exprs.literals;

import builtins.BuiltinType;
import parser.Ast.exprs.Expr;

public final class IntLiteral extends Expr {

    public IntLiteral(int si, int ei, int sc, int ec, int ln) {
        super(si, ei, sc, ec, ln);
        type = BuiltinType.Integer;
    }
}
