package com.styleshow.components;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.styleshow.R;
import com.styleshow.adapters.PostCarouselAdapter;
import com.styleshow.adapters.PostPreviewAdapter;
import com.styleshow.common.ClickableRecyclerAdapter;
import com.styleshow.common.ItemClickListener;
import com.styleshow.common.ThemeColor;
import com.styleshow.databinding.ViewDynamicPostsBinding;
import com.styleshow.domain.model.Post;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import timber.log.Timber;

// TODO: change switcher to use Toggle Button:
// https://github.com/material-components/material-components-android/blob/master/docs/components/Button.md#toggle-button

// FIXME: for some reason the top row of the grid isn't sized correctly
// i have no idea why

/**
 * Custom view to display either a grid or carousel of posts.
 * It contains controls to switch between the two layouts.
 * <p>
 * Using SharedPreferences to store the layout type.
 */
public class DynamicPostsView extends ConstraintLayout {

    private final List<Post> posts = new ArrayList<>();
    private final BehaviorSubject<LayoutType> layoutSubject = BehaviorSubject.create();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ViewDynamicPostsBinding binding; // final
    private SharedPreferences sharedPrefs; // final
    private ClickableRecyclerAdapter<?, Post> adapter;
    private @Nullable ItemClickListener<Post> itemClickListener;

    public DynamicPostsView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public DynamicPostsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DynamicPostsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public DynamicPostsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    /**
     * Adapter for the bg colour of the button.
     */
    @BindingAdapter("tint")
    public static void tintAdaptor(@NonNull ImageButton view, @AttrRes int themeColorRes) {
        @ColorInt int tint = ThemeColor.getThemeColor(view.getContext(), themeColorRes);
        view.getDrawable().setTint(tint);
    }

    private void init(@Nullable AttributeSet attrs) {
        var context = getContext();

        binding = ViewDynamicPostsBinding.inflate(
                LayoutInflater.from(context),
                this,
                true
        );

        binding.ibGrid.setOnClickListener(v -> setLayout(LayoutType.GRID));
        binding.ibCarousel.setOnClickListener(v -> setLayout(LayoutType.CAROUSEL));

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DynamicPostsView,
                0, 0);

        LayoutType layout = null;

        try {
            int layoutIndex = a.getInteger(R.styleable.DynamicPostsView_layoutType, -1);
            boolean useLayoutFromSharedPrefs = layoutIndex == -1;

            if (!useLayoutFromSharedPrefs)
                layout = LayoutType.values()[layoutIndex];
        } finally {
            a.recycle();
        }

        if (layout == null)
            layout = getLayoutFromSharedPrefs();

        layoutSubject.onNext(layout);
    }

    private @NonNull LayoutType getLayoutFromSharedPrefs() {
        int index = sharedPrefs.getInt(
                getContext().getString(R.string.dynamic_posts_view_sp_layout_key),
                LayoutType.getDefault().ordinal()
        );
        return LayoutType.values()[index];
    }

    private void saveLayoutToSharedPrefs(LayoutType layout) {
        String key = getContext().getString(R.string.dynamic_posts_view_sp_layout_key);

        var editor = sharedPrefs.edit();
        editor.putInt(key, layout.ordinal());
        editor.apply();
    }

    private void onLayoutChanged(@NonNull LayoutType layout) {
        Timber.d("layout changed to %s", layout);
        binding.setLayout(layout);
        saveLayoutToSharedPrefs(layout);

        switch (layout) {
            case GRID -> showGridLayout();
            case CAROUSEL -> showCarouselLayout();
        }
    }

    private void showGridLayout() {
        adapter = new PostPreviewAdapter(posts);
        adapter.setItemClickListener(itemClickListener);
        binding.rvPosts.setAdapter(adapter);

        int numColumns = getContext().getResources().getInteger(R.integer.dynamic_posts_view_grid_columns);

        binding.rvPosts.setLayoutManager(new GridLayoutManager(getContext(), numColumns));
    }

    private void showCarouselLayout() {
        adapter = new PostCarouselAdapter(posts);
        adapter.setItemClickListener(itemClickListener);
        binding.rvPosts.setAdapter(adapter);

        binding.rvPosts.setLayoutManager(new CarouselLayoutManager());
    }

    /**
     * Subscribe to changes in a lifecycle-aware manner.
     *
     * @see <a href="https://stackoverflow.com/a/66789559">https://stackoverflow.com/a/66789559</a>
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        compositeDisposable.add(
                layoutSubject
                        .distinctUntilChanged()
                        .subscribe(this::onLayoutChanged)
        );
    }

    /**
     * Dispose of subscriptions.
     *
     * @see #onAttachedToWindow()
     * @see <a href="https://stackoverflow.com/a/66789559">https://stackoverflow.com/a/66789559</a>
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        compositeDisposable.clear();
    }

    public void setPosts(@NonNull List<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public @NonNull LayoutType getLayout() {
        return layoutSubject.getValue();
    }

    public void setLayout(@NonNull LayoutType layout) {
        layoutSubject.onNext(layout);
    }

    public void setItemClickListener(@Nullable ItemClickListener<Post> itemClickListener) {
        this.itemClickListener = itemClickListener;
        if (adapter != null)
            adapter.setItemClickListener(itemClickListener);
    }

    /**
     * The layout type of the view.
     */
    public enum LayoutType {
        /**
         * A grid layout.
         *
         * @see PostPreviewAdapter
         */
        GRID,
        /**
         * A carousel layout.
         *
         * @see PostCarouselAdapter
         * @see <a href="https://m3.material.io/components/carousel/overview">Material Carousel</a>
         * @see <a href="https://github.com/material-components/material-components-android/blob/master/docs/components/Carousel.md">Carousel Documentation</a>
         */
        CAROUSEL,
        ;

        public static LayoutType getDefault() {
            return GRID;
        }
    }
}
