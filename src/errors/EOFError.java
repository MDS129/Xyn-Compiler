package errors;

public final class EOFError extends Err{
    public EOFError(String m, int l, int s, int e, Object... f) {
        super(m, l, s, e, f);
    }
}
