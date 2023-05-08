package com.styleshow.adapters;

import static com.google.android.material.animation.AnimationUtils.lerp;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ItemPostCarouselBinding;
import com.styleshow.domain.model.Post;
import com.styleshow.ui.post.PostActivity;

public class PostCarouselAdapter extends RecyclerView.Adapter<PostCarouselAdapter.PostHolder> {

    private final List<Post> posts;

    /**
     * Using this constructor means this will manage the list of posts.
     */
    public PostCarouselAdapter() {
        this(new ArrayList<>());
    }

    /**
     * Using this constructor means the caller will have to manage the list of posts.
     * The caller can make modifications to the list and call {@link #notifyDataSetChanged()}.
     *
     * @note Do not call {@link #setPosts(List)} if using this constructor with an immutable list.
     */
    public PostCarouselAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = ItemPostCarouselBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new PostHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostHolder extends RecyclerView.ViewHolder {
        final ItemPostCarouselBinding binding;

        public PostHolder(ItemPostCarouselBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("RestrictedApi")
        public void bind(Post post) {
            binding.setPost(post);

            // Open post on click
            binding.ivImage.setOnClickListener(v -> {
                var intent = new Intent(v.getContext(), PostActivity.class);
                intent.putExtra(Constants.POST_NAME, post);
                v.getContext().startActivity(intent);
            });

            // https://github.com/material-components/material-components-android/blob/master/docs/components/Carousel.md#reacting-to-changes-in-item-mask-size
            binding.container.setOnMaskChangedListener(maskRect -> {
                // Any custom motion to run when mask size changes
                binding.tvCaption.setTranslationX(maskRect.left);
                binding.tvCaption.setAlpha(lerp(1F, 0F, 0F, 400F, maskRect.left));
            });
        }
    }
}
