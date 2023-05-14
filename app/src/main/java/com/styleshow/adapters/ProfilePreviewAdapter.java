package com.styleshow.adapters;

import java.util.List;
import java.util.function.IntConsumer;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.styleshow.common.ClickableRecyclerAdapter;
import com.styleshow.databinding.ItemProfilePreviewBinding;
import com.styleshow.domain.model.UserProfile;

public class ProfilePreviewAdapter extends
        ClickableRecyclerAdapter<ProfilePreviewAdapter.ProfilePreviewHolder, UserProfile> {

    private List<UserProfile> profiles;

    public ProfilePreviewAdapter(@NonNull List<UserProfile> profiles) {
        this.profiles = profiles;
    }

    @Override
    public void setItems(@NonNull List<UserProfile> profiles) {
        this.profiles = profiles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProfilePreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var profilePreviewBinding = ItemProfilePreviewBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new ProfilePreviewHolder(profilePreviewBinding, index -> {
            onItemClick(index, profiles.get(index));
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePreviewHolder holder, int position) {
        UserProfile profile = profiles.get(position);
        holder.bind(profile);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    static class ProfilePreviewHolder extends RecyclerView.ViewHolder {
        final ItemProfilePreviewBinding binding;

        public ProfilePreviewHolder(ItemProfilePreviewBinding binding, @NonNull IntConsumer onItemClick) {
            super(binding.getRoot());
            this.binding = binding;

            this.itemView.setOnClickListener(v -> {
                onItemClick.accept(getLayoutPosition());
            });
        }

        public void bind(UserProfile profile) {
            binding.setProfile(profile);
        }
    }
}
