package ext.arch.components.dao;

import java.util.List;

/**
 * Created by ROOT on 2017/8/14.
 */

public interface IDaoOperator<T, K> {
    long add(T data);

    void deleteByKey(K id);

    void delete(T data);

    void update(T data);

    T query(K id);

    long getTotalCount();

    void addAll(List<T> list);

    List<T> getAll();

    void deleteAll();
}