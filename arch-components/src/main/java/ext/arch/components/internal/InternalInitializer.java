package ext.arch.components.internal;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.util.Log;

/**
 * Internal class to initialize.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class InternalInitializer extends ContentProvider {
    @Override
    public boolean onCreate() {
        Log.i("InternalInitializer", "init Internal initializer");
        AppMemoryRecycled.get().init(getContext());
        LifecycleDispatcher.init(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        throw new RuntimeException("Internal Initializer. Never allowed");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Internal Initializer. Never allowed");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new RuntimeException("Internal Initializer. Never allowed");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("Internal Initializer. Never allowed");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("Internal Initializer. Never allowed");
    }
}
