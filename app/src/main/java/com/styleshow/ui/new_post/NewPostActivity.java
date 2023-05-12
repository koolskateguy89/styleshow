package com.styleshow.ui.new_post;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
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

// TODO: decide on whether to instantly open image picker (current) or open it on image click

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
        //setContentView(binding.getRoot());

        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    // TODO: callback
                    if (uri != null) {
                        Timber.i("Selected URI = %s", uri);

                        // https://developer.android.com/training/data-storage/shared/photopicker#persist-media-file-access
                        // Persist access permissions so can access the file while uploading
                        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        getContentResolver().takePersistableUriPermission(uri, flag);

                        binding.ivImage.setImageURI(uri);
                        setContentView(binding.getRoot());
                    } else {
                        Timber.i("No media selected");
                        finish();
                    }
                });

        // Launch the photo picker and let the user choose only images.
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(PickVisualMedia.ImageOnly.INSTANCE)
                .build());

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

        binding.btnPublishPost.setOnClickListener(v -> {
            // TODO: view model publish post
        });
    }
}
