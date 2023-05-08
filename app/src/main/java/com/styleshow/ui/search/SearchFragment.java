package com.styleshow.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.styleshow.adapters.ProfilePreviewAdapter;
import com.styleshow.common.AfterTextChangedTextWatcher;
import com.styleshow.databinding.FragmentSearchBinding;
import dagger.hilt.android.AndroidEntryPoint;

// TODO: handle loading state

/**
 * @see <a href="https://github.com/material-components/material-components-android/blob/master/docs/components/Search.md#putting-it-all-together">
 * Material Search Component</a>
 */
@AndroidEntryPoint
public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.loadProfiles();

        binding = FragmentSearchBinding.inflate(inflater, container, false);

        binding.sv.getEditText().addTextChangedListener(new AfterTextChangedTextWatcher(s -> {
            viewModel.setQuery(s.toString());
        }));

        // Setup RecyclerView
        binding.rvProfiles.setLayoutManager(new LinearLayoutManager(getContext()));
        var previewAdapter = new ProfilePreviewAdapter();
        binding.rvProfiles.setAdapter(previewAdapter);

        viewModel.getFilteredProfiles()
                .observe(getViewLifecycleOwner(), previewAdapter::setProfiles);

        viewModel.getLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            if (loadingState == null)
                return;

            switch (loadingState) {
                case LOADING -> {
                    // Display progress indicator
                    binding.viewSwitcher.setDisplayedChild(1);
                }
                case SUCCESS_IDLE -> {
                    // Display users
                    binding.viewSwitcher.setDisplayedChild(0);
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
