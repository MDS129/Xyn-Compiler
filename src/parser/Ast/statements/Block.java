package parser.Ast.statements;

import symboltables.Symbol;
import symboltables.SymbolType;
import java.util.HashMap;

// block is a scope
public final class Block extends Statement {
    public final Statement[] statements;
    public final HashMap<SymbolType, HashMap<String, Symbol>> symbolTable = new HashMap<>();
    public Block(int size) {
        statements = new Statement[size];
        symbolTable.put(SymbolType.var, new HashMap<>());
    }
}
