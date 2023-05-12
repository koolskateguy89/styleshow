package com.styleshow.adapters;

import static com.google.android.material.animation.AnimationUtils.lerp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.styleshow.common.ClickableRecyclerAdapter;
import com.styleshow.databinding.ItemPostCarouselBinding;
import com.styleshow.domain.model.Post;

/**
 * The adapter for the carousel of post previews.
 *
 * @see com.styleshow.R.layout#item_post_carousel
 */
public class PostCarouselAdapter extends ClickableRecyclerAdapter<PostCarouselAdapter.PostHolder, Post> {

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
    public PostCarouselAdapter(@NonNull List<Post> posts) {
        this.posts = posts;
    }

    /**
     * Mutate the internal posts lists and notify of a data set change.
     */
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

        return new PostHolder(binding, index -> {
            onItemClick(posts.get(index));
        });
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

        final @NonNull ItemPostCarouselBinding binding;

        public PostHolder(@NonNull ItemPostCarouselBinding binding, @NonNull IntConsumer onItemClick) {
            super(binding.getRoot());
            this.binding = binding;

            this.itemView.setOnClickListener(v -> {
                onItemClick.accept(getLayoutPosition());
            });
        }

        @SuppressLint("RestrictedApi")
        public void bind(@NonNull Post post) {
            binding.setPost(post);

            // Update caption position when the carousel item is moved
            // https://github.com/material-components/material-components-android/blob/master/docs/components/Carousel.md#reacting-to-changes-in-item-mask-size
            binding.container.setOnMaskChangedListener(maskRect -> {
                // Any custom motion to run when mask size changes
                binding.tvCaption.setTranslationX(maskRect.left);
                binding.tvCaption.setAlpha(lerp(1F, 0F, 0F, 400F, maskRect.left));
            });
        }
    }
}
