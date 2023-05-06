package com.styleshow.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ItemPostPreviewBinding;
import com.styleshow.domain.model.Post;
import com.styleshow.ui.post.PostActivity;

public class PostPreviewAdapter extends RecyclerView.Adapter<PostPreviewAdapter.PostPreviewHolder> {

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

        return new PostPreviewHolder(postPreviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostPreviewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostPreviewHolder extends RecyclerView.ViewHolder {
        final ItemPostPreviewBinding binding;

        public PostPreviewHolder(ItemPostPreviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Post post) {
            binding.setPost(post);

            binding.ivImage.setOnClickListener(v -> {
                var intent = new Intent(v.getContext(), PostActivity.class);
                intent.putExtra(Constants.POST_NAME, post);
                v.getContext().startActivity(intent);
            });
        }
    }
}
