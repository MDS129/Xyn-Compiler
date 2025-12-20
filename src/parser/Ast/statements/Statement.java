package parser.Ast.statements;

import parser.Ast.AstNode;
import parser.Ast.statements.decls.Decl;

public sealed class Statement extends AstNode permits Block, Decl {

    protected Statement(int si, int ei, int sc, int ec, int ln) {
        super(si, ei, sc, ec, ln);
    }
    protected Statement() {super(0, 0, 0, 0, 0);}
}
