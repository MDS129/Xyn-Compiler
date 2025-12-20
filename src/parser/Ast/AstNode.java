package parser.Ast;

import parser.Ast.exprs.Expr;
import parser.Ast.statements.Statement;

// the grand-grand father of all
public sealed class AstNode permits Expr, Statement {
    public final int startIdx, endIdx, startCol, endCol, line;

    protected AstNode(int si, int ei, int sc, int ec, int ln) {
        startIdx = si;
        endIdx = ei;
        startCol = sc;
        endCol = ec;
        line = ln;
    }
}
