package com.styleshow.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
- [ ] swipe-to-refresh, see https://developer.android.com/develop/ui/views/touch-and-input/swipe
 */

// TODO: add loading skeleton, at least for search, possibly for posts too
// maybe not cos the 3 top-level screens using the same loading is nice
// see https://medium.com/android-dev-nation/android-skeleton-loaders-1ae979a9d8c9

// TODO: pin search bar at top, not yet sure how to tho

// TODO: only get like 10 images initially, then on reached bottom and swipe down, get more

/**
 * The home screen, showing posts and a search bar to find profiles.
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

        var makeNewPost = registerForActivityResult(new NewPostActivity.NewPostContract(),
                resultCode -> {
                    switch (resultCode) {
                        case NewPostActivity.RESULT_POST_CREATED -> {
                            // reload posts
                            viewModel.loadPosts();
                        }
                        case NewPostActivity.RESULT_POST_NOT_CREATED -> {
                            // do nothing
                            Timber.d("Post not created");
                        }
                    }
                });

        binding.fabNewPost.setOnClickListener(v -> {
            makeNewPost.launch(null);
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

        // Handle profile loading state
        viewModel.getSearchLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            switch (loadingState) {
                case LOADING -> {
                    // Display progress indicator
                    binding.viewSwitcherSearch.setDisplayedChild(1);
                }
                case SUCCESS_IDLE -> {
                    // Display profiles
                    binding.viewSwitcherSearch.setDisplayedChild(0);
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
