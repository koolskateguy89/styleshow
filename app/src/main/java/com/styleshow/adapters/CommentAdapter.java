package com.styleshow.adapters;

import java.util.List;
import java.util.function.IntConsumer;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.styleshow.common.ClickableRecyclerAdapter;
import com.styleshow.databinding.ItemCommentBinding;
import com.styleshow.domain.model.Comment;

public class CommentAdapter extends ClickableRecyclerAdapter<CommentAdapter.CommentHolder, Comment> {

    private @NonNull List<Comment> comments;

    public CommentAdapter(@NonNull List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public void setItems(@NonNull List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = ItemCommentBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new CommentHolder(binding, index -> {
            onItemClick(index, comments.get(index));
        });
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        var comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    static class CommentHolder extends RecyclerView.ViewHolder {
        final ItemCommentBinding binding;

        public CommentHolder(ItemCommentBinding binding, @NonNull IntConsumer onItemClick) {
            super(binding.getRoot());
            this.binding = binding;

            this.itemView.setOnClickListener(v -> {
                onItemClick.accept(getLayoutPosition());
            });
        }

        public void bind(Comment comment) {
            binding.setComment(comment);
        }
    }
}
