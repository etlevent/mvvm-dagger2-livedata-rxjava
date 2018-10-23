package ext.arch.components.json;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class NumberNullSafeAdapter extends NullSafeTypeAdapter<Number> {

    public static final int TOKEN_INT = 0;
    public static final int TOKEN_DOUBLE = 1;
    public static final int TOKEN_LONG = 2;
    public static final int TOKEN_FLOAT = 3;

    @IntDef({TOKEN_INT,
            TOKEN_LONG,
            TOKEN_FLOAT,
            TOKEN_DOUBLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NumberToken {

    }

    @NumberNullSafeAdapter.NumberToken
    private int mNumberToken = TOKEN_INT;

    public NumberNullSafeAdapter() {
        this(TOKEN_INT);
    }

    public NumberNullSafeAdapter(@NumberNullSafeAdapter.NumberToken int numberToken) {
        super(0);
        this.mNumberToken = numberToken;
    }

    @Override
    protected void writeNullSafe(JsonWriter out, @NonNull Number value) throws IOException {
        out.value(value);
    }

    @Override
    protected Number readNullSafe(JsonReader in) throws IOException {
        switch (mNumberToken) {
            case TOKEN_INT:
                return in.nextInt();
            case TOKEN_DOUBLE:
                return in.nextDouble();
            case TOKEN_LONG:
                return in.nextLong();
            case TOKEN_FLOAT:
                return (float) in.nextDouble();
            default:
                throw new UnsupportedOperationException("NumberToken Unsupported. " + mNumberToken);
        }
    }
}
