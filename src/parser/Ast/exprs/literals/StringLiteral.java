package parser.Ast.exprs.literals;

import builtins.BuiltinType;
import parser.Ast.exprs.Expr;

public final class StringLiteral extends Expr {

    public StringLiteral(int si, int ei, int sc, int ec, int ln) {
        super(si, ei, sc, ec, ln);
        type = BuiltinType.String;
    }
}
