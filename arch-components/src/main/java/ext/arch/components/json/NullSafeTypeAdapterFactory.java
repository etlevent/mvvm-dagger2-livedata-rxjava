package ext.arch.components.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public class NullSafeTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final Class<T> rawType = (Class<T>) type.getRawType();
        if (String.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) new StringNullSafeAdapter();
        }
        return null;
    }
}
