package com.styleshow.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.styleshow.databinding.ItemPostBinding;
import com.styleshow.domain.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private final List<Post> posts;

    /**
     * Using this constructor means this will manage the list of posts.
     */
    public PostAdapter() {
        this(new ArrayList<>());
    }

    /**
     * Using this constructor means the caller will have to manage the list of posts.
     * The caller can make modifications to the list and call {@link #notifyDataSetChanged()}.
     *
     * @note Do not call {@link #setPosts(List)} if using this constructor with an immutable list.
     */
    public PostAdapter(@NonNull List<Post> posts) {
        this.posts = posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }

    @Override
    public @NonNull PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var postPreviewBinding = ItemPostBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new PostHolder(postPreviewBinding);
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
        final ItemPostBinding binding;

        public PostHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Post post) {
            binding.setPost(post);
        }
    }
}
