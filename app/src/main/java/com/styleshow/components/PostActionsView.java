package com.styleshow.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.styleshow.R;
import com.styleshow.databinding.ViewPostActionsBinding;

/**
 * View that contains the actions for a post. Does not contain any logic, just UI.
 *
 * <ul>
 *     <li>Like (with counter)</li>
 *     <li>Comment (with counter)</li>
 *     <li>Share</li>
 *     <li>Delete (if deletable)</li>
 * </ul>
 *
 * @see R.styleable#PostActionsView
 * @see R.layout#view_post_actions
 */
public class PostActionsView extends ConstraintLayout {

    private ViewPostActionsBinding binding;
    private boolean liked;
    private int numLikes;
    private int numComments;
    private boolean deletable;

    public PostActionsView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public PostActionsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PostActionsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public PostActionsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        binding = ViewPostActionsBinding.inflate(
                LayoutInflater.from(getContext()),
                this,
                true
        );

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PostActionsView,
                0, 0);

        try {
            liked = a.getBoolean(R.styleable.PostActionsView_liked, false);
            numLikes = a.getInt(R.styleable.PostActionsView_numLikes, 0);
            numComments = a.getInt(R.styleable.PostActionsView_numComments, 0);
            deletable = a.getBoolean(R.styleable.PostActionsView_deletable, false);
        } finally {
            a.recycle();
        }

        binding.setLiked(liked);
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
        binding.setLiked(liked);
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
        binding.setNumLikes(numLikes);
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
        binding.setNumComments(numComments);
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
        binding.setDeletable(deletable);
    }

    public void setOnLikeClickListener(OnClickListener listener) {
        binding.btnLike.setOnClickListener(listener);
    }

    public void setOnCommentClickListener(OnClickListener listener) {
        binding.btnComment.setOnClickListener(listener);
    }

    public void setOnShareClickListener(OnClickListener listener) {
        binding.btnShare.setOnClickListener(listener);
    }

    public void setOnDeleteClickListener(OnClickListener listener) {
        binding.btnDelete.setOnClickListener(listener);
    }
}
