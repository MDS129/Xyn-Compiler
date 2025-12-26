package LIRs;

import builtins.BuiltinType;

public class VarDesc {
    public BuiltinType type;
    public String value;
    public VarDesc(BuiltinType t, String v) {
        type = t;
        value = v;
    }
    @Override
    public String toString() {
        return String.format("{type: %s, value: %s}", type, value);
    }
}
