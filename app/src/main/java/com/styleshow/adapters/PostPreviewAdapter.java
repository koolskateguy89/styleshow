package com.styleshow.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ItemPostPreviewBinding;
import com.styleshow.domain.model.Post;
import com.styleshow.ui.post.PostActivity;

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
                    .into(binding.ivImage);

            binding.ivImage.setOnClickListener(v -> {
                var intent = new Intent(v.getContext(), PostActivity.class);
                intent.putExtra(Constants.POST_NAME, post);
                v.getContext().startActivity(intent);
            });
        }
    }
}
