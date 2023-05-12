package com.styleshow.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.styleshow.common.ClickableRecyclerAdapter;
import com.styleshow.databinding.ItemPostPreviewBinding;
import com.styleshow.domain.model.Post;

/**
 * The adapter for the grid of post previews.
 *
 * @see com.styleshow.R.layout#item_post_preview
 */
public class PostPreviewAdapter extends ClickableRecyclerAdapter<PostPreviewAdapter.PostPreviewHolder, Post> {

    private final List<Post> posts;

    /**
     * Using this constructor means this will manage the list of posts.
     */
    public PostPreviewAdapter() {
        this(new ArrayList<>());
    }

    /**
     * Using this constructor means the caller will have to manage the list of posts.
     * The caller can make modifications to the list and call {@link #notifyDataSetChanged()}.
     *
     * @note Do not call {@link #setPosts(List)} if using this constructor with an immutable list.
     */
    public PostPreviewAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostPreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var postPreviewBinding = ItemPostPreviewBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new PostPreviewHolder(postPreviewBinding, index -> {
            onItemClick(posts.get(index));
        });
    }

    @Override
    public void onBindViewHolder(@NonNull PostPreviewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
        holder.itemView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostPreviewHolder extends RecyclerView.ViewHolder {

        final @NonNull ItemPostPreviewBinding binding;

        public PostPreviewHolder(@NonNull ItemPostPreviewBinding binding, @NonNull IntConsumer onItemClick) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.ivImage.setOnClickListener(v -> {
                onItemClick.accept(getLayoutPosition());
            });
        }

        public void bind(Post post) {
            binding.setPost(post);
        }
    }
}
