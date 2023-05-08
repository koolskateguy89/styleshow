package com.styleshow.components;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.styleshow.R;
import com.styleshow.adapters.PostCarouselAdapter;
import com.styleshow.adapters.PostPreviewAdapter;
import com.styleshow.common.Constants;
import com.styleshow.common.Utils;
import com.styleshow.databinding.ViewDynamicPostsBinding;
import com.styleshow.domain.model.Post;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import timber.log.Timber;

// TODO!: use sharedpreferences to store layout type for persistence

/**
 * Custom view to display either a grid or carousel of posts.
 * It contains controls to switch between the two layouts.
 */
public class DynamicPostsView extends ConstraintLayout {

    private final List<Post> posts = new ArrayList<>();
    private final BehaviorSubject<LayoutType> layoutSubject = BehaviorSubject.create();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ViewDynamicPostsBinding binding;
    private RecyclerView.Adapter<?> adapter;

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

    private void init(@Nullable AttributeSet attrs) {
        binding = ViewDynamicPostsBinding.inflate(
                LayoutInflater.from(getContext()),
                this,
                true
        );

        binding.ibGrid.setOnClickListener(v -> setLayout(LayoutType.GRID));
        binding.ibCarousel.setOnClickListener(v -> setLayout(LayoutType.CAROUSEL));

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DynamicPostsView,
                0, 0);

        try {
            int layoutIndex = a.getInteger(R.styleable.DynamicPostsView_layoutType, 0);
            layoutSubject.onNext(LayoutType.values()[layoutIndex]);
        } finally {
            a.recycle();
        }
    }

    private void onLayoutChanged(@NonNull LayoutType layout) {
        Timber.d("layout changed to %s", layout);
        binding.setLayout(layout);

        switch (layout) {
            case GRID -> showGridLayout();
            case CAROUSEL -> showCarouselLayout();
        }
    }

    private void showGridLayout() {
        adapter = new PostPreviewAdapter(posts);
        binding.rvPosts.setAdapter(adapter);

        binding.rvPosts.setLayoutManager(
                new GridLayoutManager(getContext(), Constants.NUMBER_OF_POST_PREVIEW_COLUMNS));

        binding.ibGrid.getBackground().setTint(Utils.getPrimaryColor(getContext()));
        binding.ibCarousel.getBackground().setTint(Utils.getSecondaryColor(getContext()));
    }

    private void showCarouselLayout() {
        adapter = new PostCarouselAdapter(posts);
        binding.rvPosts.setAdapter(adapter);

        binding.rvPosts.setLayoutManager(new CarouselLayoutManager());

        binding.ibCarousel.getBackground().setTint(Utils.getPrimaryColor(getContext()));
        binding.ibGrid.getBackground().setTint(Utils.getSecondaryColor(getContext()));
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

    public LayoutType getLayout() {
        return layoutSubject.getValue();
    }

    public void setLayout(@NonNull LayoutType layout) {
        layoutSubject.onNext(layout);
    }

    /**
     * The layout type of the view.
     */
    public enum LayoutType {
        /**
         * A grid layout.
         *
         * @see Constants#NUMBER_OF_POST_PREVIEW_COLUMNS
         * @see PostPreviewAdapter
         */
        GRID,
        /**
         * A carousel layout.
         *
         * @see PostCarouselAdapter
         * @see <a href="https://m3.material.io/components/carousel/overview">Material Carousel</a>
         * @see https://github.com/material-components/material-components-android/blob/master/docs/components/Carousel.md
         */
        CAROUSEL,
        ;
    }
}
