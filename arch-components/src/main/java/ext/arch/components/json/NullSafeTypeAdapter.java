package ext.arch.components.json;

import android.support.annotation.NonNull;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public abstract class NullSafeTypeAdapter<T> extends TypeAdapter<T> {
    private T mNullSafeValue;

    public NullSafeTypeAdapter(T nullSafeValue) {
        this.mNullSafeValue = nullSafeValue;
    }

    @Override
    public final void write(JsonWriter out, T value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        writeNullSafe(out, value);
    }

    @Override
    public final T read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return this.mNullSafeValue;
        }
        return readNullSafe(in);
    }

    protected abstract void writeNullSafe(JsonWriter out, @NonNull T value) throws IOException;

    protected abstract T readNullSafe(JsonReader in) throws IOException;
}
