package ext.arch.components.dao;

import android.support.annotation.NonNull;

import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

import ext.arch.components.util.Preconditions;

/**
 * @author roothost
 * @date 2018/2/5
 */

public final class DaoFactory {
    private static final ConcurrentHashMap<Class, IDaoOperator> _operatorMap = new ConcurrentHashMap<>();

    private DaoFactory() {
        throw new AssertionError("No instance");
    }

    public static <D extends IDaoOperator> D getDaoOperator(@NonNull Class<? extends D> daoClass) {
        try {
            Preconditions.checkNotNull(daoClass);
            if (daoClass.isInterface() || Modifier.isAbstract(daoClass.getModifiers())) {
                throw new IllegalArgumentException("class can't be interface or abstract.");
            }
            IDaoOperator operator = _operatorMap.get(daoClass);
            if (operator == null) {
                operator = daoClass.newInstance();
                _operatorMap.put(daoClass, operator);
            }
            return (D) _operatorMap.get(daoClass);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
