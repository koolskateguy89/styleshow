package com.styleshow.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.styleshow.R;
import com.styleshow.common.NoOpTransformation;
import jp.wasabeef.transformers.picasso.CropCircleTransformation;
import jp.wasabeef.transformers.picasso.CropSquareTransformation;
import jp.wasabeef.transformers.picasso.GrayscaleTransformation;
import timber.log.Timber;

/**
 * A custom ImageView that uses Picasso to load images.
 * <p>
 * It also supports applying pre-defined transformations to the image,
 * see {@link TransformationEnum}.
 *
 * @see <a href="https://github.com/square/picasso">Picasso</a>
 */
public class PicassoImageView extends androidx.appcompat.widget.AppCompatImageView {

    private @NonNull Transformation transformation = new NoOpTransformation();

    public PicassoImageView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public PicassoImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PicassoImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * Initialize the view using the attributes provided in the XML.
     */
    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PicassoImageView,
                0, 0);

        try {
            int transformationEnumIndex = a.getInteger(R.styleable.PicassoImageView_transform, 0);
            Timber.d("transformationEnumIndex: %s", transformationEnumIndex);

            var transformationEnum = TransformationEnum.values()[transformationEnumIndex];
            Timber.d("transformationEnum: %s", transformationEnum);

            this.transformation = transformationEnum.transformation;
        } finally {
            a.recycle();
        }
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        Picasso.get()
                .load(uri)
                .transform(transformation)
                .into(this);
    }

    /**
     * Transformation enum for transformations provided by the Transformers library
     * (and a no-op transformation).
     * <p>
     * Only the transformations that take no parameters are supported.
     *
     * @see <a href="https://github.com/wasabeef/transformers">Transformers</a>
     */
    public enum TransformationEnum {
        NONE(new NoOpTransformation()),
        CROP_CIRCLE(new CropCircleTransformation()),
        CROP_SQUARE(new CropSquareTransformation()),
        GRAYSCALE(new GrayscaleTransformation()),
        ;

        final @NonNull Transformation transformation;

        TransformationEnum(@NonNull Transformation transformation) {
            this.transformation = transformation;
        }
    }
}
