package ext.arch.components.util;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jdk8.function.Function;
import jdk8.function.Predicate;

public final class ArchCollections {
    private ArchCollections() {
        throw new AssertionError("no instance.");
    }

    public static <T> void filter(@NonNull Collection<T> collection, @NonNull Predicate<T> predicate) {
        Preconditions.checkNotNull(predicate);
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.next())) {
                iterator.remove();
            }
        }
    }

    public static <T, R> List<R> mapList(@NonNull List<T> src, @NonNull Function<T, R> map) {
        Preconditions.checkNotNull(map);
        if (src.isEmpty()) {
            return Collections.emptyList();
        }
        List<R> dst = new ArrayList<>(src.size());
        for (T t : src) {
            dst.add(map.apply(t));
        }
        return dst;
    }
}
