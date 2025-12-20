package parser.Ast.statements.decls;

import parser.Ast.statements.Statement;

public sealed abstract class Decl extends Statement permits VarDecl {
    protected Decl(int si, int ei, int sc, int ec, int ln) {
        super(si, ei, sc, ec, ln);
    }
}
