package ext.arch.components.model;

public interface Response<T> {
    T body();

    boolean isSuccess();

    RuntimeException error();
}
