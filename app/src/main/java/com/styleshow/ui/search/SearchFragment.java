package com.styleshow.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.styleshow.adapters.ProfilePreviewAdapter;
import com.styleshow.databinding.FragmentSearchBinding;
import dagger.hilt.android.AndroidEntryPoint;

// TODO: handle loading state

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

        binding.sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setQuery(newText);
                return false;
            }
        });

        // Setup RecyclerView
        binding.rvProfiles.setLayoutManager(new LinearLayoutManager(getContext()));
        var previewAdapter = new ProfilePreviewAdapter();
        binding.rvProfiles.setAdapter(previewAdapter);

        viewModel.getFilteredProfiles()
                .observe(getViewLifecycleOwner(), previewAdapter::setProfiles);

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
