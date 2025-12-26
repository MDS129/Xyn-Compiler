package LIRs;

import builtins.BuiltinType;

import java.util.HashMap;
import java.util.List;

public final class LIRGen {

    private final StringBuilder sb = new StringBuilder();
    private final StringBuilder other = new StringBuilder();

    private final HashMap<String, VarDesc> variable = new HashMap<>();
    private final HashMap<String, VarDesc> prevVars;

    public LIRGen(HashMap<String, VarDesc> var) {
         prevVars = var;
    }

    private char byType(BuiltinType t) {
        return switch (t) {
            case Integer -> 'i';
            case Float -> 'f';
            case String -> 's';
            default -> throw new Error("Unknown type");
        };
    }

    public String generate() {
        List<String> keys = prevVars.keySet().stream().toList();
        for (String k : keys) {
            VarDesc varDesc = prevVars.get(k);
            char c = byType(varDesc.type);
            sb.append("push_").append(c).append(' ').append(varDesc.value).append('\n');
            sb.append("store_").append(c).append(' ').append(k).append('\n');
        }

        return sb.toString();
    }
}
