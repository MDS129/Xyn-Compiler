package errors;

public final class DummyError extends Err{
    public DummyError(String m, int l, int s, int e, Object... f) {
        super(m, l, s, e, f);
    }
}
