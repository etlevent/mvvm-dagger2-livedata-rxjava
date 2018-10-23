package ext.arch.components.json;

import android.support.annotation.NonNull;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class StringNullSafeAdapter extends NullSafeTypeAdapter<String> {

    public StringNullSafeAdapter() {
        super("");
    }

    @Override
    protected void writeNullSafe(JsonWriter out, @NonNull String value) throws IOException {
        out.value(value);
    }

    @Override
    protected String readNullSafe(JsonReader in) throws IOException {
        return in.nextString();
    }
}
