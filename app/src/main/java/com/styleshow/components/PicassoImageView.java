package com.styleshow.components;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.squareup.picasso.Picasso;

public class PicassoImageView extends androidx.appcompat.widget.AppCompatImageView {
    public PicassoImageView(@NonNull Context context) {
        super(context);
    }

    public PicassoImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PicassoImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        Picasso.get()
                .load(uri)
                .into(this);
    }
}
