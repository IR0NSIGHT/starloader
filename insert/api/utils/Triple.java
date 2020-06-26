package api.utils;

public class Triple<T,U,V> {
    private final T t;
    private final U u;
    private final V v;

    public Triple(T t, U u, V v) {
        this.t = t;
        this.u = u;
        this.v = v;
    }

    public T getT() {
        return t;
    }

    public U getU() {
        return u;
    }

    public V getV() {
        return v;
    }

}
