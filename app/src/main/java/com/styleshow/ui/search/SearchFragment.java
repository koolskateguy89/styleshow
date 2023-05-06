package com.styleshow.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.styleshow.adapters.ProfilePreviewAdapter;
import com.styleshow.databinding.FragmentSearchBinding;
import dagger.hilt.android.AndroidEntryPoint;

// TODO: handle loading state

// TODO: have a look at SearchView

@AndroidEntryPoint
public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel viewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.loadProfiles();

        binding = FragmentSearchBinding.inflate(inflater, container, false);

        binding.etQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setQuery(s.toString());
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
    }
}
