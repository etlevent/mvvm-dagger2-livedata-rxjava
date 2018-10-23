package ext.arch.components.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import ext.arch.components.R;
import io.reactivex.annotations.CheckReturnValue;

public abstract class ToolbarFragment extends BaseFragment {
    private static final String TAG = "ToolbarFragment";
    private static final int INVALID_ID = -1;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private ImageButton mNavigationButton;
    private FrameLayout mToolbarContainer;
    private ActionMenuView mActionMenuView;
    private FrameLayout mContentContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.arch_fragment_toolbar, container, false);
        mAppBarLayout = parentView.findViewById(R.id.app_bar_layout);
        mCollapsingToolbarLayout = parentView.findViewById(R.id.collapsing_toolbar_layout);
        mToolbar = parentView.findViewById(R.id.toolbar);
        mNavigationButton = parentView.findViewById(android.R.id.home);
        mToolbarContainer = parentView.findViewById(R.id.toolbar_container);
        mActionMenuView = parentView.findViewById(R.id.action_menu_view);
        mContentContainer = parentView.findViewById(R.id.content_container);

        generateToolbar();
        generateContent(inflater, container);
        return parentView;
    }

    private void generateContent(LayoutInflater inflater, @Nullable ViewGroup container) {
        // inflate content
        if (getContentLayoutId() != INVALID_ID && getContentView(inflater, container) != null) {
            throw new IllegalArgumentException("getContentLayoutId() and getContentView() only one can be called.");
        } else if (getContentLayoutId() != INVALID_ID) {
            getLayoutInflater().inflate(getContentLayoutId(), mContentContainer, true);
        } else if (getContentView(inflater, container) != null) {
            mContentContainer.addView(getContentView(inflater, container));
        }
    }

    private void generateToolbar() {
        // inflate toolbar
        final View toolbarContent;
        if (getToolbarContentLayoutId() != INVALID_ID && getToolbarContentView() != null) {
            throw new IllegalArgumentException("getToolbarContentLayoutId() and getToolbarContentView() only one can be called.");
        } else if (getToolbarContentLayoutId() != INVALID_ID) {
            toolbarContent = getLayoutInflater().inflate(getToolbarContentLayoutId(), mToolbarContainer, false);
        } else {
            toolbarContent = getToolbarContentView();
        }
        ViewGroup.LayoutParams params;
        if (toolbarContent.getLayoutParams() != null) {
            params = new FrameLayout.LayoutParams(toolbarContent.getLayoutParams());
        } else {
            params = new FrameLayout.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
        }
        mToolbarContainer.addView(getToolbarContentView(), 0, params);
        onCreateOptionsMenu(mToolbar.getMenu(), getActivity().getMenuInflater());
        setDisplayHomeAsUpEnabled(false);
        mToolbar.setNavigationOnClickListener(v -> finishSelf());
        mToolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        mActionMenuView.setOnMenuItemClickListener(this::onOptionsItemSelected);
        mNavigationButton.setOnClickListener(this::onNavigationClick);
    }

    protected void setDisplayHomeAsUpEnabled(boolean enabled) {
        mNavigationButton.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    @LayoutRes
    protected int getContentLayoutId() {
        return INVALID_ID;
    }

    @CheckReturnValue
    protected View getContentView(LayoutInflater inflater, @Nullable ViewGroup container) {
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

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    protected ActionMenuView getActionMenuView() {
        return mActionMenuView;
    }

    protected CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return mCollapsingToolbarLayout;
    }

    protected AppBarLayout getAppBarLayout() {
        return mAppBarLayout;
    }

    protected TextView createCompatTextView() {
        TextView textView = new AppCompatTextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return textView;
    }

//    protected void addFragment(Fragment fragment) {
//        getChildFragmentManager().beginTransaction()
//                .add(R.id.root, fragment)
//                .addToBackStack(null)
//                .commit();
//    }
//
//    protected void finishFragment(Fragment fragment) {
//        getChildFragmentManager().beginTransaction()
//                .remove(fragment)
//                .commit();
//    }

    protected void finishSelf() {
        final FragmentManager fm;
        if (getParentFragment() != null) {
            fm = getParentFragment().getChildFragmentManager();
        } else {
            fm = getActivity().getSupportFragmentManager();
        }
        fm.beginTransaction()
                .remove(this)
                .commit();
    }

    protected void onNavigationClick(@NonNull View view) {

    }
}
