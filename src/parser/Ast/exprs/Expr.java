package parser.Ast.exprs;

import builtins.BuiltinType;
import parser.Ast.AstNode;
import parser.Ast.exprs.literals.FloatLiteral;
import parser.Ast.exprs.literals.IntLiteral;
import parser.Ast.exprs.literals.StringLiteral;

public sealed class Expr extends AstNode permits BinaryExpr,
                                                 UnaryExpr,
                                                 IntLiteral,
                                                 FloatLiteral,
                                                 StringLiteral
{
    public BuiltinType type;

    protected Expr(int si, int ei, int sc, int ec, int ln) {
        super(si, ei, sc, ec, ln);
    }
}
