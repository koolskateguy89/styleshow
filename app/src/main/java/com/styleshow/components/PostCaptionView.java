package com.styleshow.components;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.styleshow.R;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ViewPostCaptionBinding;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.ui.user_profile.UserProfileActivity;

public class PostCaptionView extends ConstraintLayout {

    private ViewPostCaptionBinding binding;
    private String caption;
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

        binding.ivImage.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UserProfileActivity.class);
            intent.putExtra(Constants.PROFILE_NAME, author);
            getContext().startActivity(intent);
        });
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
        binding.setCaption(caption);
    }

    public UserProfile getAuthor() {
        return author;
    }

    public void setAuthor(UserProfile author) {
        this.author = author;
        binding.setAuthor(author);
    }
}
