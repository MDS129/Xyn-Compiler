package IRs;

import builtins.BuiltinType;
import errors.ErrorEngine;
import parser.Ast.AstNode;
import parser.Ast.exprs.BinaryExpr;
import parser.Ast.exprs.Expr;
import parser.Ast.exprs.UnaryExpr;
import parser.Ast.statements.decls.VarDecl;
import symboltables.Symbol;
import symboltables.SymbolType;

import java.util.HashMap;



// optimization class for IR
final class IRPass {
    private boolean improvement = false;
    private final StringBuilder sb = new StringBuilder(500);
    private int idx = 0;
    private final char[] chars;

    private final HashMap<Integer, Pair<BuiltinType, Object>> var = new HashMap<>();
    private int counter = 0;

    public IRPass(char[] c) {
        chars = c;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void handleVar() {
        final StringBuilder sb = new StringBuilder();
        while (isDigit(chars[idx])) {
            sb.append(chars[idx]);
        }
        final int varIdx = Integer.parseInt(sb.toString());
        ++counter;
        sb.setLength(0);
        // skipping " = "
        idx += 3;
        switch (chars[idx]) {
            // const
            case 'c': {
                // skipping "const "
                idx += 6;
                // Integer value
                if (isDigit(chars[idx])) {
                    while (isDigit(chars[idx])) {
                        sb.append(chars[idx]);
                    }
                    var.put(varIdx, new Pair<>(BuiltinType.Integer, Integer.parseInt(sb.toString())));
                    // skipping the newline
                    ++idx;
                }
                break;
            }
            case 'f':
            case 'i':
                if (chars[idx+1] == 't' && chars[idx+2] == 'o') {
                    idx += 2;
                    char toType = chars[idx];
                    idx += 2;
                    if (chars[idx] == 't') {
                        ++idx;
                        while (isDigit(chars[idx])) {
                            sb.append(chars[idx]);
                        }
                        final int b = Integer.parseInt(sb.toString());
                        var.replace(b, new Pair<>(
                                (toType == 'i') ? BuiltinType.Integer : BuiltinType.Float,
                                (toType == 'i') ?
                                        Integer.parseInt(String.valueOf(var.get(b).right()))
                                        :
                                        Float.parseFloat(String.valueOf(var.get(b).right()))

                        ));
                    }
                }
        }

    }

    public void pass() {
        if (chars[idx] == 't') {
            ++idx;
            handleVar();
        }
    }
    public boolean hasNewImprovement() {
        return improvement && !(improvement = false);
    }
}

public final class IRGen {
    private final StringBuilder sb = new StringBuilder(1000);

    // borrowing for a sec, sorry ErrorEngine
    private final String source = ErrorEngine.source;
    private final HashMap<SymbolType, HashMap<String, Symbol>> symbolTable;
    private final AstNode[] statements;
    private final int length;
    private int counter = 0;
    public IRGen(AstNode[] s, HashMap<SymbolType, HashMap<String, Symbol>> sy, int l) {
        statements = s;
        symbolTable = sy;
        length = l;
    }

    private final HashMap<Integer, BuiltinType> reg = new HashMap<>(3);

    private void makeBinary(BinaryExpr bin) {
        evalExpr(bin.left);
        int left = counter-1;
        evalExpr(bin.right);
        int right = counter-1;
        if ((reg.get(left) == BuiltinType.Integer && reg.get(right) == BuiltinType.Float) || (reg.get(right) == BuiltinType.Integer && reg.get(left) == BuiltinType.Float)) {
            sb.append('t').append(counter++).append(" = itof t").append((reg.get(left) == BuiltinType.Integer) ? left : right).append('\n');
            if (reg.get(left) == BuiltinType.Integer) {
                left = counter-1;
            } else {
                right = counter-1;
            }
            reg.put(counter, BuiltinType.Float);
        } else {
            reg.put(counter, BuiltinType.Integer);
        }
        
        sb.append('t').append(counter++).append(" = ").append(
                switch (bin.op) {
                    case Plus -> "add";
                    case Minus -> "sub";
                    case Mul -> "mul";
                    case Div -> "div";
                    default -> throw new Error("wth");
                }
                ).append(' ')
                .append('t').append(left)
                .append(" t").append(right)
                .append('\n');
    }

    private void evalExpr(AstNode value) {
        if (value instanceof BinaryExpr b) {
            makeBinary(b);
            return;
        }
        if (value instanceof UnaryExpr u) {
            evalExpr(u.expr);
            return;
        }
        if (value instanceof Expr e) {
            reg.put(counter, e.type);
            sb.append('t').append(counter++).append(" = const ").append(source, value.startIdx, value.endIdx).append('\n');
            return;
        }
        throw new Error("What? How");
    }

    private void generateDecl(VarDecl var) {
        AstNode val = var.value;
        evalExpr(val);
        int v = counter-1;
        if (var.varType != BuiltinType.Inferred && var.varType != reg.get(v)) {
            sb.append('t').append(counter++).append(" = ").append(
                    switch (reg.get(v)) {
                        case Integer -> "ito";
                        case Float -> "fto";
                        default ->  throw new Error("WTH");
                    }
            ).append(
                    switch (var.varType) {
                        case Integer -> "i";
                        case Float -> "f";
                        default ->  throw new Error("WTH");
                    }
            ).append(" t").append(v).append('\n');
        }
        sb.append("store ").append(source, var.startIdx, var.endIdx).append(' ').append('t').append(counter-1).append('\n');
    }

    public void generate() {
        int elm = 0;
        while (elm < length) {
            switch (statements[elm++]) {
                case VarDecl v:
                    generateDecl(v);
                    IO.println(sb.toString());
                    break;
                default:
                    break;
            }
        }
    }
}
