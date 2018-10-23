package ext.arch.components.dao;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author roothost
 */
public abstract class AbstractDaoOperator<T, K, D extends AbstractDao<T, K>>
        implements IDaoOperator<T, K> {

    protected final D mDao;

    public AbstractDaoOperator() {
        this.mDao = getDao();
    }

    @Override
    public long add(T data) {
        return mDao != null ? mDao.insertOrReplace(data) : -1;
    }

    @Override
    public void deleteByKey(K id) {
        if (mDao != null) {
            mDao.deleteByKey(id);
        }
    }

    @Override
    public void delete(T data) {
        if (mDao != null) {
            mDao.delete(data);
        }
    }

    @Override
    public void update(T data) {
        if (mDao != null) {
            mDao.update(data);
        }
    }

    @Override
    public T query(K id) {
        return mDao != null ? mDao.load(id) : null;
    }

    @Override
    public long getTotalCount() {
        if (mDao != null) {
            QueryBuilder<T> queryBuilder = mDao.queryBuilder();
            return queryBuilder.buildCount().count();
        }
        return 0;
    }

    @Override
    public List<T> getAll() {
        return mDao != null ? mDao.loadAll() : null;
    }

    @Override
    public void addAll(List<T> list) {
        if (mDao != null) {
            mDao.insertOrReplaceInTx(list);
        }
    }

    @Override
    public void deleteAll() {
        if (mDao != null) {
            mDao.deleteAll();
        }
    }

    @NonNull
    protected abstract D getDao();
}