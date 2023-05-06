package com.styleshow.components;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ViewPostPreviewGridBinding;
import com.styleshow.domain.model.Post;
import com.styleshow.adapters.PostPreviewAdapter;

public class PostPreviewGrid extends ConstraintLayout {

    private ViewPostPreviewGridBinding binding;
    private PostPreviewAdapter previewAdapter;

    public PostPreviewGrid(@NonNull Context context) {
        super(context);
        initView();
    }

    public PostPreviewGrid(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PostPreviewGrid(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public PostPreviewGrid(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    /**
     * Inflate the view and bind it to the layout.
     *
     * @see https://stackoverflow.com/a/17413018
     */
    private void initView() {
        binding = ViewPostPreviewGridBinding.inflate(
                LayoutInflater.from(getContext()),
                this,
                true
        );

        // Setup recycler view
        binding.rv.setLayoutManager(
                new GridLayoutManager(getContext(), Constants.NUMBER_OF_POST_PREVIEW_COLUMNS));
        previewAdapter = new PostPreviewAdapter();
        binding.rv.setAdapter(previewAdapter);
    }

    public void setPosts(List<Post> posts) {
        previewAdapter.setPosts(posts);
    }
}
