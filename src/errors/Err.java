package errors;

// my own error class
public abstract class Err {
    public final String msg;
    /// for error reporting only
    public final int line, startIdx, endIdx;

    public Err(String m, int l, int s, int e) {
        msg = "\u001B[97m" + m + "\u001B[0m";
        line = l;
        startIdx = s;
        endIdx = e;
    }

    public Err(String m, int l, int s, int e, Object... f) {
        this(String.format(m, f), l, s, e);
    }
}
