package symboltables;

import builtins.BuiltinType;
import parser.Ast.AstNode;

public final class Variable extends Symbol {
    public final boolean isDynamic;
    public BuiltinType type;
    public final AstNode value;

    public Variable(boolean id, BuiltinType t, AstNode v) {
        isDynamic = id;
        type = t;
        value = v;
    }
}
