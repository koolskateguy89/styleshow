package com.styleshow.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.styleshow.R;
import com.styleshow.common.NoOpTransformation;
import jp.wasabeef.transformers.picasso.CropCircleTransformation;
import jp.wasabeef.transformers.picasso.CropSquareTransformation;
import jp.wasabeef.transformers.picasso.GrayscaleTransformation;

/**
 * A custom ImageView that uses Picasso to load images.
 * <p>
 * It also supports applying pre-defined transformations to the image,
 * see {@link TransformationEnum}.
 *
 * @apiNote Do NOT instantiate this in code, use it in XML only.
 * @see R.styleable#PicassoImageView
 * @see <a href="https://github.com/square/picasso">Picasso</a>
 */
public class PicassoImageView extends AppCompatImageView {

    /**
     * Whether to display a placeholder image while the image is loading.
     *
     * @see com.squareup.picasso.RequestCreator#noPlaceholder()
     */
    private boolean noPlaceholder;

    /**
     * The placeholder image to display while the image is loading.
     *
     * @see com.squareup.picasso.RequestCreator#placeholder(int)
     */
    @DrawableRes
    private int placeholderResId;

    /**
     * The transformation to apply to the image.
     *
     * @see com.squareup.picasso.RequestCreator#transform(Transformation)
     */
    private @NonNull Transformation transformation;

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
            noPlaceholder = a.getBoolean(R.styleable.PicassoImageView_noPlaceholder, false);
            placeholderResId = a.getResourceId(R.styleable.PicassoImageView_placeholder, R.drawable.img_placeholder);

            int transformationIndex = a.getInteger(R.styleable.PicassoImageView_transform, TransformationEnum.getDefault().ordinal());
            transformation = TransformationEnum.values()[transformationIndex].transformation;
        } finally {
            a.recycle();
        }
    }

    /**
     * Load the image from the given URI using Picasso.
     *
     * @see Picasso
     */
    @Override
    public void setImageURI(@Nullable Uri uri) {
        var requestCreator = Picasso.get()
                .load(uri)
                .transform(transformation);

        if (noPlaceholder) {
            requestCreator = requestCreator.noPlaceholder();
        } else {
            requestCreator = requestCreator.placeholder(placeholderResId);
        }

        requestCreator.into(this);
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
        /**
         * No-op transformation. Does not change the image.
         *
         * @see NoOpTransformation
         */
        NONE(new NoOpTransformation()),
        /**
         * Crop the image to a circle.
         *
         * @see CropCircleTransformation
         */
        CROP_CIRCLE(new CropCircleTransformation()),
        /**
         * Crop the image to a square.
         *
         * @see CropSquareTransformation
         */
        CROP_SQUARE(new CropSquareTransformation()),
        /**
         * Convert the image to grayscale.
         *
         * @see GrayscaleTransformation
         */
        GRAYSCALE(new GrayscaleTransformation()),
        ;

        final @NonNull Transformation transformation;

        TransformationEnum(@NonNull Transformation transformation) {
            this.transformation = transformation;
        }

        public static @NonNull TransformationEnum getDefault() {
            return NONE;
        }
    }
}
