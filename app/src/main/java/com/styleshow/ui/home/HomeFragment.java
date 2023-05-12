package com.styleshow.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.styleshow.adapters.PostAdapter;
import com.styleshow.databinding.FragmentHomeBinding;
import com.styleshow.ui.messages.MessagesActivity;
import com.styleshow.ui.new_post.NewPostActivity;
import dagger.hilt.android.AndroidEntryPoint;

/*
TODO:
- [ ] show posts
- [ ] topbar kinda thing with StyleShow logo
- [x] open MessagesActivity
  - [ ] Properly customise button that does this (icon etc.)
- [ ] floating action button to create new post
 */

// TODO: on long swipe up at the top, refresh posts

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.loadPosts();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        binding.btnMessages.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MessagesActivity.class);
            startActivity(intent);
        });

        binding.fabNewPost.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewPostActivity.class);
            startActivity(intent);
        });

        // Setup recycler view
        binding.rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        var postAdapter = new PostAdapter();
        binding.rvPosts.setAdapter(postAdapter);

        viewModel.getPosts().observe(getViewLifecycleOwner(), postAdapter::setPosts);

        viewModel.getLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            if (loadingState == null)
                return;

            switch (loadingState) {
                case LOADING -> {
                    // Display progress indicator
                    binding.viewSwitcher.setDisplayedChild(1);
                }
                case SUCCESS_IDLE -> {
                    // Display posts
                    binding.viewSwitcher.setDisplayedChild(0);
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel = null;
    }
}
