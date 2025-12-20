package parser.Ast.statements.decls;

import builtins.BuiltinType;
import parser.Ast.AstNode;

public final class VarDecl extends Decl{
    public final AstNode value;

    public BuiltinType varType;
    public final boolean isDynamic;

    public VarDecl(int is, int ie, AstNode val, int sc, int ec, int ln, BuiltinType vt, boolean id) {
        super(is, ie, sc, ec, ln);
        value = val;
        varType = vt;
        isDynamic = id;
    }

    @Override
    public String toString() {
        return String.format("{type: VarDecl, var: %d:%d, col: %d:%d, line: %d}", startIdx, endIdx, startCol, endCol, line);
    }
}
