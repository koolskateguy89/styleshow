package com.styleshow.ui.new_post;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NewPostFormState {

    public final @NonNull String caption;
    public final @Nullable Uri imageUri;
    public final String shoeUrl; // might have to change to Uri

    NewPostFormState(
            @NonNull String caption,
            @Nullable Uri imageUri,
            String shoeUrl
    ) {
        this.caption = caption;
        this.imageUri = imageUri;
        this.shoeUrl = shoeUrl;
    }

    public boolean isDataValid() {
        // TODO
        return imageUri != null;
    }

    NewPostFormState withCaption(@NonNull String caption) {
        return new NewPostFormState(caption, imageUri, shoeUrl);
    }

    NewPostFormState withImageUri(@Nullable Uri imageUri) {
        return new NewPostFormState(caption, imageUri, shoeUrl);
    }

    NewPostFormState withShoeUrl(String shoeUrl) {
        return new NewPostFormState(caption, imageUri, shoeUrl);
    }
}
