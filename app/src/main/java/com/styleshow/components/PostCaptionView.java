package com.styleshow.components;

import java.util.Date;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.styleshow.R;
import com.styleshow.databinding.ViewPostCaptionBinding;
import com.styleshow.domain.model.UserProfile;

/**
 * View that contains the caption for a post. Does not contain any logic, just UI.
 * <p>
 * Includes:
 * <ul>
 *     <li>Author's profile picture</li>
 *     <li>Author's username</li>
 *     <li>Post postedAt timestamp</li>
 *     <li>Post caption</li>
 * </ul>
 *
 * @see R.styleable#PostCaptionView
 * @see R.layout#view_post_caption
 */
public class PostCaptionView extends ConstraintLayout {

    private ViewPostCaptionBinding binding;
    private String caption;
    private Date postedAt;
    private UserProfile author;

    public PostCaptionView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public PostCaptionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PostCaptionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public PostCaptionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        binding = ViewPostCaptionBinding.inflate(
                LayoutInflater.from(getContext()),
                this,
                true);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PostCaptionView,
                0, 0);

        try {
            caption = a.getString(R.styleable.PostCaptionView_caption);
        } finally {
            a.recycle();
        }

        binding.setCaption(caption);
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
        binding.setCaption(caption);
    }

    public Date getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Date postedAt) {
        this.postedAt = postedAt;
        binding.setPostedAt(postedAt);
    }

    public UserProfile getAuthor() {
        return author;
    }

    public void setAuthor(UserProfile author) {
        this.author = author;
        binding.setAuthor(author);
    }
}
