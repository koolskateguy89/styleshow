package com.styleshow.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;

/**
 * A custom LinearLayout that is always square, based on its width.
 *
 * @see <a href="https://stackoverflow.com/a/24416992">https://stackoverflow.com/a/24416992</a>
 */
public class SquareFrameLayout extends FrameLayout {

    public SquareFrameLayout(Context context) {
        super(context);
    }

    public SquareFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
    }
}
