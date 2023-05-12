package com.styleshow.ui.new_post;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.R;
import com.styleshow.common.AfterTextChangedTextWatcher;
import com.styleshow.databinding.ActivityNewPostBinding;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/*
TODO
- [x] add image picker

shoeUrl "provider" - can we use like a web view intent? - get the url they wer eon
could just do a tf tbf
 */

// TODO: intent filter for image share action, see https://developer.android.com/training/basics/intents/filters

/**
 * @see <a href="https://developer.android.com/training/data-storage/shared/photopicker#select-single-item">Image picker</a>
 */
@AndroidEntryPoint
public class NewPostActivity extends AppCompatActivity {

    private ActivityNewPostBinding binding;
    private NewPostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(NewPostViewModel.class);

        binding = ActivityNewPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Timber.i("Selected URI = %s", uri);
                        viewModel.imageChanged(uri);

                        // https://developer.android.com/training/data-storage/shared/photopicker#persist-media-file-access
                        // Persist access permissions so can access the file while uploading
                        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        getContentResolver().takePersistableUriPermission(uri, flag);
                    } else {
                        Toast.makeText(this, R.string.no_image_selected, Toast.LENGTH_LONG).show();
                    }
                });

        binding.ivImage.setOnClickListener(v -> {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        // TODO: etShoeUrl or something

        binding.etCaption.addTextChangedListener(new AfterTextChangedTextWatcher(s -> {
            viewModel.captionChanged(s.toString());
        }));
    }
}
