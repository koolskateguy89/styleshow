package com.styleshow.ui.new_post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.R;
import com.styleshow.common.AfterTextChangedTextWatcher;
import com.styleshow.databinding.ActivityNewPostBinding;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * Activity for creating a new post. The user can select an image, enter a caption and a
 * shoe URL, and then post it.
 *
 * @see <a href="https://developer.android.com/training/data-storage/shared/photopicker#select-single-item">Image picker</a>
 */
@AndroidEntryPoint
public class NewPostActivity extends AppCompatActivity {

    public static final int RESULT_POST_CREATED = 42;
    public static final int RESULT_POST_NOT_CREATED = 24;

    private ActivityNewPostBinding binding;
    private NewPostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        @Nullable Uri imageUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        viewModel = new ViewModelProvider(this).get(NewPostViewModel.class);
        if (imageUri != null)
            viewModel.setImageUri(imageUri);

        binding = ActivityNewPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        // Registers a photo picker activity launcher in single-select mode.
        var pickMedia = registerForActivityResult(new PickVisualMedia(), uri -> {
            if (uri != null) {
                Timber.i("Selected URI = %s", uri);
                viewModel.setImageUri(uri);

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

        binding.etCaption.addTextChangedListener(new AfterTextChangedTextWatcher(s -> {
            viewModel.setCaption(s.toString());
        }));

        binding.etShoeUrl.addTextChangedListener(new AfterTextChangedTextWatcher(s -> {
            viewModel.setShoeUrl(s.toString());
        }));

        binding.btnPost.setOnClickListener(v -> {
            if (viewModel.isDataValid()) {
                viewModel.publishPost();
            } else {
                Toast.makeText(this, R.string.invalid_post, Toast.LENGTH_SHORT).show();
            }
        });

        // Handle loading state (when uploading post)
        viewModel.getLoadingState().observe(this, loadingState -> {
            switch (loadingState) {
                case LOADING -> {
                    binding.btnPost.setEnabled(false);
                }
                case SUCCESS_IDLE -> {
                    Toast.makeText(this, R.string.post_successful, Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
                case ERROR -> {
                    Toast.makeText(this, R.string.post_failed, Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }
        });
    }

    /**
     * Contract for launching {@link NewPostActivity}. Result is {@link #RESULT_POST_CREATED} or
     * {@link #RESULT_POST_NOT_CREATED}.
     */
    public static class NewPostContract extends ActivityResultContract<Uri, Integer> {

        @Override
        public @NonNull Intent createIntent(@NonNull Context context, @Nullable Uri imageUri) {
            return new Intent(context, NewPostActivity.class)
                    .putExtra(Intent.EXTRA_STREAM, imageUri);
        }

        @Override
        public Integer parseResult(int resultCode, @Nullable Intent intent) {
            return resultCode == RESULT_OK ? RESULT_POST_CREATED : RESULT_POST_NOT_CREATED;
        }
    }
}
