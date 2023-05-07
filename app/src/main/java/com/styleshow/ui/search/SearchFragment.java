package com.styleshow.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import timber.log.Timber;

// TODO: handle loading state

/**
 * @see <a href="https://github.com/material-components/material-components-android/blob/master/docs/components/Search.md#putting-it-all-together">
 *     Material Search Component</a>
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

        binding.sv.getEditText().addTextChangedListener(new TextWatcher() {
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

        //binding.sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        //    @Override
        //    public boolean onQueryTextSubmit(String query) {
        //        return false;
        //    }
        //
        //    @Override
        //    public boolean onQueryTextChange(String newText) {
        //        viewModel.setQuery(newText);
        //        return false;
        //    }
        //});

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
