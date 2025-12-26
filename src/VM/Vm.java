package VM;

import LIRs.VarDesc;
import builtins.BuiltinType;

import java.util.HashMap;
import java.util.Stack;

public final class Vm {
    private String lir;
    private final char[] lirChars;
    private final Stack<VarDesc> stack = new Stack<>();
    private final HashMap<String, VarDesc> variables = new HashMap<>();

    private final StringBuilder other = new StringBuilder();


    private int idx = 0;
    private final int lirLen;
    public Vm(String lir) {
        this.lir = lir;
        this.lirChars = lir.toCharArray();
        this.lirLen = lir.length();
    }

    private void skipIdx(int i) {
        idx += i;
    }

    private boolean isDigitChar(char c) {
        return c >= '0' && c <= '9';
    }

    private void gatherDigit() {
        while (idx < lirLen && isDigitChar(lirChars[idx])) {
            other.append(lirChars[idx++]);
        }
    }

    private void gatherUntil(char endChar) {
        while (idx < lirLen && lirChars[idx] != endChar) {
            other.append(lirChars[idx++]);
        }
    }

    private void reset(StringBuilder sb) {
        sb.setLength(0);
    }

    private BuiltinType getType() {
        return switch (lirChars[idx]) {
            case 'i' -> BuiltinType.Integer;
            case 'f' -> BuiltinType.Float;
            case 's' -> BuiltinType.String;
            default -> throw new Error("Unknown Type");
        };
    }


    public void execute() {
        while (idx < lirLen) {
            lir = lir.substring(idx);
            if (lir.startsWith("push")) {
                skipIdx(5); // skip the "push_"
                BuiltinType type = getType();
                skipIdx(2); // skip the "i " or "f " or "s "
                gatherUntil('\n');
                stack.push(new VarDesc(type, other.toString()));
                reset(other);
                skipIdx(1); // skip the newline
            }
            lir = lir.substring(idx);
            if (lir.startsWith("store")) {
                skipIdx(6); // skip the "store_"
                BuiltinType type = getType();
                skipIdx(2); // skip the "i " or "f " or "s "
                gatherUntil('\n');
                String varName = other.toString();
                VarDesc value = stack.pop();
                if (value.type != type) {
                    throw new Error("Type mismatch on store operation");
                }
                variables.put(varName, value);
                reset(other);
                skipIdx(1); // skip the newline
            }
        }
        IO.println(variables);
    }
}
