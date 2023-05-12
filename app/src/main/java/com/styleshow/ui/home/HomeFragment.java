package com.styleshow.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.adapters.PostAdapter;
import com.styleshow.adapters.ProfilePreviewAdapter;
import com.styleshow.common.AfterTextChangedTextWatcher;
import com.styleshow.databinding.FragmentHomeBinding;
import com.styleshow.ui.new_post.NewPostActivity;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/*
TODO:
- [x] show posts
- [x] floating action button to create new post
  - [x] make it movable, see https://stackoverflow.com/a/46373935/
- [ ] on long swipe up at the top, refresh posts
 */

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.loadPosts();
        viewModel.setup();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        // Create new post
        binding.fabNewPost.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewPostActivity.class);
            startActivity(intent);
        });

        // Setup post recycler view
        var postAdapter = new PostAdapter();
        binding.rvPosts.setAdapter(postAdapter);
        viewModel.getPosts().observe(getViewLifecycleOwner(), postAdapter::setPosts);

        binding.sv.getEditText().addTextChangedListener(new AfterTextChangedTextWatcher(s -> {
            viewModel.setSearchQuery(s.toString());
        }));

        // Handle post loading state
        viewModel.getPostLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
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

        // Setup profile recycler view
        var profilePreviewAdapter = new ProfilePreviewAdapter();
        binding.rvProfiles.setAdapter(profilePreviewAdapter);

        viewModel.getFilteredProfiles()
                .observe(getViewLifecycleOwner(), profilePreviewAdapter::setProfiles);

        // TODO: handle search loading state
        viewModel.getSearchLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            switch (loadingState) {
                case LOADING -> {
                    // Display progress indicator
                    //binding.viewSwitcher.setDisplayedChild(1);
                    Toast.makeText(requireContext(), "Loading!", Toast.LENGTH_SHORT).show();
                    Timber.i("Loading!");
                }
                case SUCCESS_IDLE -> {
                    // Display posts
                    //binding.viewSwitcher.setDisplayedChild(0);
                    Toast.makeText(requireContext(), "Done!", Toast.LENGTH_SHORT).show();
                    Timber.i("Done!");
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel.dispose();
        viewModel = null;
    }
}
