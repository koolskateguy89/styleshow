package com.styleshow.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.databinding.FragmentHomeBinding;
import com.styleshow.ui.messages.MessagesActivity;

/*
TODO:
- [ ] show posts
- [ ] topbar kinda thing with StyleShow logo
- [x] open MessagesActivity
  - [ ] Properly customise button that does this (icon etc.)
 */

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        viewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding.btnMessages.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MessagesActivity.class);
            startActivity(intent);
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
