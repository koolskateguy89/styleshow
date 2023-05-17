package com.styleshow.adapters;

import java.util.List;
import java.util.function.IntConsumer;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.styleshow.common.ItemClickListener;
import com.styleshow.databinding.ItemPostBinding;
import com.styleshow.domain.model.Post;

/**
 * The adapter for the list of posts.
 *
 * @see com.styleshow.R.layout#item_post
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private @NonNull List<Post> posts;
    private @Nullable ItemClickListener<Post> imageClickListener;
    private @Nullable ItemClickListener<Post> captionClickListener;

    public PostAdapter(@NonNull List<Post> posts) {
        this.posts = posts;
    }

    public void setPosts(@NonNull List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @Override
    public @NonNull PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = ItemPostBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new PostHolder(binding, index -> {
            if (imageClickListener != null)
                imageClickListener.onClick(index, posts.get(index));
        }, index -> {
            if (captionClickListener != null)
                captionClickListener.onClick(index, posts.get(index));
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

    public void setImageClickListener(@Nullable ItemClickListener<Post> imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

    public void setCaptionClickListener(@Nullable ItemClickListener<Post> captionClickListener) {
        this.captionClickListener = captionClickListener;
    }

    static class PostHolder extends RecyclerView.ViewHolder {
        final @NonNull ItemPostBinding binding;

        public PostHolder(
                @NonNull ItemPostBinding binding,
                @NonNull IntConsumer onImageClick,
                @NonNull IntConsumer onCaptionClick
        ) {
            super(binding.getRoot());
            this.binding = binding;

            binding.ivImage.setOnClickListener(v -> {
                onImageClick.accept(getLayoutPosition());
            });

            binding.viewPostCaption.setOnClickListener(v -> {
                onCaptionClick.accept(getLayoutPosition());
            });
        }

        public void bind(Post post) {
            binding.setPost(post);
        }
    }
}
