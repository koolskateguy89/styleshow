package com.styleshow.components;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ViewPostAuthorBinding;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.ui.user_profile.UserProfileActivity;

/**
 * A view that displays the author of a post.
 * <p>
 * It displays the author's profile picture and username.
 * <p>
 * Clicking on either of these elements opens the author's profile.
 */
public class PostAuthor extends ConstraintLayout {

    private ViewPostAuthorBinding binding;

    public PostAuthor(@NonNull Context context) {
        super(context);
        initView();
    }

    public PostAuthor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PostAuthor(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public PostAuthor(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    /**
     * Initialises the view. Inflates the layout using the binding.
     */
    private void initView() {
        binding = ViewPostAuthorBinding.inflate(
                LayoutInflater.from(getContext()),
                this,
                true
        );

        // On click image or name, open profile
        binding.ivImage.setOnClickListener(v -> openProfile());
        binding.tvUsername.setOnClickListener(v -> openProfile());
    }

    /**
     * Open profile activity for the author of the post.
     */
    private void openProfile() {
        var profile = binding.getAuthor();

        if (profile == null)
            return;

        var intent = new Intent(getContext(), UserProfileActivity.class);
        intent.putExtra(Constants.PROFILE_NAME, profile);
        getContext().startActivity(intent);
    }

    /**
     * Set the author of the post. To be used to initialise the view.
     *
     * @param author The author of the post.
     */
    public void setAuthor(UserProfile author) {
        binding.setAuthor(author);
    }
}
