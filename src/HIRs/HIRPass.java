package HIRs;

import LIRs.VarDesc;
import builtins.BuiltinType;

import java.util.HashMap;

/// optimization class for IR
public final class HIRPass {
    /// a flag that indicates if the previous expr are operation
    private boolean afterOperation = false;

    /// for main stringBuilder
    private StringBuilder sb = new StringBuilder(500);

    /// for other purposes
    private final StringBuilder other = new StringBuilder(500);

    private int idx = 0;
    private char[] chars;


    private final HashMap<Integer, VarDesc> reg = new HashMap<>();
    public final HashMap<String, VarDesc> var = new HashMap<>();

    public HIRPass(char[] c) {
        chars = c;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }


    private void skipIdx(int i) {
        idx += i;
    }

    private void gatherIndex() {
        while (isDigit(chars[idx])) {
            other.append(chars[idx++]);
        }
    }

    private void gatherUntil(char c) {
        while (chars[idx] != c) {
            other.append(chars[idx++]);
        }
    }

    private char byType(BuiltinType t) {
        return switch (t) {
            case Integer -> 'i';
            case Float -> 'f';
            case String -> 's';
            default -> throw new Error("Unknown type");
        };
    }

    private int getFirstArg(int i) {
        afterOperation = true;
        // skip the "add "
        skipIdx(4);
        // example: "t0 = const "
        sb.append('t').append(i).append(" = const ");

        int v0 = -1;
        // first argument
        if (chars[idx] == 't') {
            skipIdx(1);
            gatherIndex();

            v0 = Integer.parseInt(other.toString());
        }
        skipIdx(1); // skip the space between first arg and second arg
        reset(other);
        sb.append(byType(reg.get(v0).type)).append(' ');
        return v0;
    }

    /// 0 for addition, 1 for subtraction, 2 for multiplication, and 3 for division
    private void handleOperation(int op, int index) {

        final int v0 = getFirstArg(index);

        skipIdx(1);
        gatherIndex();

        // the first argument is temp var
        if (v0 >= 0) {
            final String left = reg.get(v0).value;
            final String right = reg.get(Integer.parseInt(other.toString())).value;
            final String value = String.valueOf(
                    switch (op) {
                        case 0 -> Integer.parseInt(left) + Integer.parseInt(right);
                        case 1 -> Integer.parseInt(left) - Integer.parseInt(right);
                        case 2 -> Integer.parseInt(left) * Integer.parseInt(right);
                        case 3 -> Integer.parseInt(left) / Integer.parseInt(right);
                        default -> throw new Error("Unknown operator");
                    }
            );

            reg.put(index, new VarDesc(reg.get(v0).type, value));
            sb.append(value).append('\n');
        }
    }

    private BuiltinType getType() {
        return switch (chars[idx]) {
            case 'i' -> BuiltinType.Integer;
            case 'f' -> BuiltinType.Float;
            case 's' -> BuiltinType.String;
            default -> throw new Error("Unknown Type");
        };
    }

    private void handleTempVarValue(int index) {
        switch (chars[idx]) {
            // const
            case 'c':
                // skipping "const "
                skipIdx(6);
                BuiltinType type = getType();
                skipIdx(2);
                if (chars[idx] == '-') {
                    other.append(chars[idx++]);
                }
                if (isDigit(chars[idx])) {
                    gatherIndex();
                    reg.put(index, new VarDesc(type, other.toString()));
                } else { // var
                    gatherUntil('\n');
                    reg.put(index, new VarDesc(type, var.get(other.toString()).value));
                }
                break;

            // add
            case 'a': {
                handleOperation(0, index);
                break;
            }
            // sub
            case 's': {
                handleOperation(1, index);
                break;
            }
            // mul
            case 'm': {
                handleOperation(2, index);
                break;
            }
            // div
            case 'd': {
                handleOperation(3, index);
                break;
            }
        }
        skipIdx(1); // skip the newline
        reset(other);
    }

    private void handleTempVar() {
        // skipping "t"
        skipIdx(1);
        gatherIndex();

        final int index = Integer.parseInt(other.toString());

        reset(other);
        skipIdx(3); // skipping " = "

        handleTempVarValue(index);

    }

    private void reset(StringBuilder s) {
        s.setLength(0);
    }

    private void handleVar() {
        // skipping the "store "
        skipIdx(6);

        other.append("store ");
        // skipping the varName
        gatherUntil(' ');
        other.append(chars[idx++]);
        other.append(chars[idx++]);

        final String st = other.toString();

        // skipping the space between value type and value
        skipIdx(1);

        reset(other);
        // skip
        if (isDigit(chars[idx])) {
            gatherUntil('\n');
            reset(other);
            skipIdx(1);
            return;
        }
        final String key = st.substring(6, st.length() - 2);
        if (chars[idx] == 't') {
            skipIdx(1); // skipping the t

            gatherIndex();
            final int right = Integer.parseInt(other.toString());

            if (afterOperation) {
                sb.append(st).append(' ').append('t').append(right).append('\n');
            } else {
                sb.append(st).append(' ').append(reg.get(right).value).append('\n');
            }
            // skip the "store "
            var.put(key, new VarDesc(reg.get(right).type, reg.get(right).value));
        } else {
            gatherUntil('\n');

            final String right = other.toString();
            sb.append(st).append(' ').append(var.get(right).value).append('\n');
            // skip the "store "
            var.put(key, var.get(right));
        }
        skipIdx(1); // skipping the new line
        reset(other);
    }

    public String optimize() {
        onePass();
        IO.println("\n- first pass:");
        IO.println(sb.toString());
        chars = sb.toString().toCharArray();
        sb = new StringBuilder();

        onePass();
        IO.println("\n- second pass:");
        return sb.toString();
    }

    private void onePass() {
        boolean hasImprovement = true;
        idx = 0;
        reg.clear();

        while (hasImprovement) {
            hasImprovement = false;

            if (idx >= chars.length) break;

            if (chars[idx] == '\n') {
                hasImprovement = true;
                ++idx;
                continue;
            }
            // temp var
            if (chars[idx] == 't') {
                handleTempVar();
                hasImprovement = true;
            }
            // permanent var
            if (chars[idx] == 's') {
                handleVar();
                hasImprovement = true;
            }

            afterOperation = false;
        }
    }

}
