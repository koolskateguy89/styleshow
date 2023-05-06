package com.styleshow.adapters;

// TODO: onclick open user profile activity

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.styleshow.databinding.ItemProfilePreviewBinding;
import com.styleshow.domain.model.UserProfile;

public class ProfilePreviewAdapter extends RecyclerView.Adapter<ProfilePreviewAdapter.ProfilePreviewHolder> {

    private final List<UserProfile> profiles = new ArrayList<>();

    public void setProfiles(List<UserProfile> profiles) {
        this.profiles.clear();
        this.profiles.addAll(profiles);
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

        return new ProfilePreviewHolder(profilePreviewBinding);
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

        public ProfilePreviewHolder(ItemProfilePreviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserProfile profile) {
            binding.tvUsername.setText(profile.getUsername());

            Picasso.get()
                    .load(profile.getProfilePictureUrl())
                    .into(binding.ivImage);

            binding.ivImage.setOnClickListener(v -> {
                // TODO
            });
        }
    }
}
