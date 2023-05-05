package com.styleshow.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.styleshow.databinding.ItemPostPreviewBinding;
import com.styleshow.domain.model.Post;

public class PostPreviewAdapter extends RecyclerView.Adapter<PostPreviewAdapter.PostPreviewHolder> {

    private final List<Post> posts = new ArrayList<>();

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
            Picasso.get()
                    .load(post.getImageUrl())
                    .resize(200, 200)
                    .centerCrop()
                    .into(binding.ivImage);
        }
    }
}
