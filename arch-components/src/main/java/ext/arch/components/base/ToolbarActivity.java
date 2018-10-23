package ext.arch.components.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ext.arch.components.R;
import io.reactivex.annotations.CheckReturnValue;


public class ToolbarActivity extends BaseActivity {

    private static final int INVALID_ID = -1;
    private Toolbar mToolbar;
    private ViewGroup mContentContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arch_activity_toolbar);
        mToolbar = findViewById(R.id.toolbar);
        mContentContainer = findViewById(R.id.container);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        generateToolbar();
        generateContent();
    }

    private void generateContent() {
        // inflate content
        if (getContentLayoutId() != INVALID_ID && getContentView() != null) {
            throw new IllegalArgumentException("getContentLayoutId() and getContentView() only one can be called.");
        } else if (getContentLayoutId() != INVALID_ID) {
            getLayoutInflater().inflate(getContentLayoutId(), mContentContainer, true);
        } else if (getContentView() != null) {
            mContentContainer.addView(getContentView());
        }
    }

    private void generateToolbar() {
        // inflate toolbar
        if (getToolbarContentLayoutId() != INVALID_ID && getToolbarContentView() != null) {
            throw new IllegalArgumentException("getToolbarContentLayoutId() and getToolbarContentView() only one can be called.");
        } else if (getToolbarContentLayoutId() != INVALID_ID) {
            getLayoutInflater().inflate(getToolbarContentLayoutId(), mToolbar, true);
        } else if (getToolbarContentView() != null) {
            Toolbar.LayoutParams params;
            if (getToolbarContentView().getLayoutParams() != null) {
                params = new Toolbar.LayoutParams(getToolbarContentView().getLayoutParams());
            } else {
                params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
            }
            params.gravity = Gravity.CENTER;
            mToolbar.addView(getToolbarContentView(), params);
        }
    }

    @LayoutRes
    protected int getContentLayoutId() {
        return INVALID_ID;
    }

    @CheckReturnValue
    protected View getContentView() {
        return null;
    }

    @LayoutRes
    protected int getToolbarContentLayoutId() {
        return INVALID_ID;
    }

    @CheckReturnValue
    protected View getToolbarContentView() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
