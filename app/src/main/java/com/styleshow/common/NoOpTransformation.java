package com.styleshow.common;

import android.graphics.Bitmap;
import com.squareup.picasso.Transformation;

/**
 * A transformation that does nothing.
 *
 * @see com.styleshow.components.PicassoImageView
 */
public class NoOpTransformation implements Transformation {

    @Override
    public Bitmap transform(Bitmap source) {
        return source;
    }

    @Override
    public String key() {
        return "NoOpTransformation()";
    }
}
