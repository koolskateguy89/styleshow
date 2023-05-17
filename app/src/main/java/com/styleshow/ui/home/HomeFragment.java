package com.styleshow.ui.home;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import com.styleshow.R;
import com.styleshow.adapters.PostAdapter;
import com.styleshow.adapters.ProfilePreviewAdapter;
import com.styleshow.common.AfterTextChangedTextWatcher;
import com.styleshow.common.Constants;
import com.styleshow.databinding.FragmentHomeBinding;
import com.styleshow.domain.model.Post;
import com.styleshow.ui.new_post.NewPostActivity;
import com.styleshow.ui.post.PostActivity;
import com.styleshow.ui.user_profile.UserProfileActivity;
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
 * The home screen, showing posts, a user search bar, and a floating action button
 * to create a new post.
 */
@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.loadPosts();
        viewModel.setup();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        var openPost = registerForActivityResult(new PostActivity.OpenPostContract(), result -> {
            if (result == null)
                return;

            if (result instanceof PostActivity.PostResult.LikeChanged likeChanged) {
                int index = likeChanged.index;
                Post post = likeChanged.post;

                viewModel.postUpdated(index, post);
                binding.rvPosts.getAdapter().notifyItemChanged(index, post);
            } else if (result instanceof PostActivity.PostResult.PostDeleted postDeleted) {
                // Inform user of deletion
                Snackbar.make(binding.getRoot(), R.string.delete_post_success,
                                Snackbar.LENGTH_SHORT)
                        .show();

                int index = postDeleted.index;

                viewModel.postDeleted(index);
                binding.rvPosts.getAdapter().notifyItemRemoved(index);
            }
        });

        var makeNewPost = registerForActivityResult(new NewPostActivity.NewPostContract(), resultCode -> {
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
        var postAdapter = new PostAdapter(List.of());
        // Open post on image click
        postAdapter.setImageClickListener((index, post) -> {
            openPost.launch(new Pair<>(index, post));
        });
        // Open profile on caption click
        postAdapter.setCaptionClickListener((index, post) -> {
            var intent = new Intent(requireContext(), UserProfileActivity.class)
                    .putExtra(Constants.PROFILE_NAME, post.getAuthor());
            startActivity(intent);
        });

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
        var profilePreviewAdapter = new ProfilePreviewAdapter(List.of());
        // On click open user profile activity, display the clicked user's profile
        profilePreviewAdapter.setItemClickListener((index, profile) -> {
            var intent = new Intent(requireContext(), UserProfileActivity.class)
                    .putExtra(Constants.PROFILE_NAME, profile);
            startActivity(intent);
        });

        binding.rvProfiles.setAdapter(profilePreviewAdapter);

        viewModel.getFilteredProfiles()
                .observe(getViewLifecycleOwner(), profilePreviewAdapter::setItems);

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

        // Handle send action to create new post
        var args = getArguments();
        if (args != null && args.containsKey(Intent.EXTRA_STREAM)) {
            Uri imageUri = args.getParcelable(Intent.EXTRA_STREAM);
            Timber.i("Received image uri: %s", imageUri);
            makeNewPost.launch(imageUri);
        }

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
