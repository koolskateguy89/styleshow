package com.styleshow.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// TODO: save position to shared preferences, with a key specific to this
// instance of the FAB, so that we can have multiple FABs with different
// positions.
// maybe provide the sharedprefs key as an attribute in the xml
// - do it in ACTION_MOVE

/**
 * FloatingActionButton that can be moved around by the user.
 *
 * @see <a href="https://stackoverflow.com/a/46373935">https://stackoverflow.com/a/46373935</a>
 */
public class MovableFloatingActionButton extends FloatingActionButton implements View.OnTouchListener {

    /**
     * Often, there will be a slight, unintentional, drag when the user taps the FAB, so we
     * need to account for this.
     */
    private final static float CLICK_DRAG_TOLERANCE = 10;

    private float downRawX, downRawY;
    private float dX, dY;

    public MovableFloatingActionButton(@NonNull Context context) {
        super(context);
        init();
    }

    public MovableFloatingActionButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MovableFloatingActionButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    /**
     * Move the FAB to wherever the user's finger is.
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        int action = motionEvent.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN -> {
                downRawX = motionEvent.getRawX();
                downRawY = motionEvent.getRawY();
                dX = view.getX() - downRawX;
                dY = view.getY() - downRawY;

                return true; // Consumed
            }
            case MotionEvent.ACTION_MOVE -> {
                int viewWidth = view.getWidth();
                int viewHeight = view.getHeight();

                View viewParent = (View) view.getParent();
                int parentWidth = viewParent.getWidth();
                int parentHeight = viewParent.getHeight();

                float newX = motionEvent.getRawX() + dX;
                newX = Math.max(layoutParams.leftMargin, newX); // Don't allow the FAB past the left hand side of the parent
                newX = Math.min(parentWidth - viewWidth - layoutParams.rightMargin, newX); // Don't allow the FAB past the right hand side of the parent

                float newY = motionEvent.getRawY() + dY;
                newY = Math.max(layoutParams.topMargin, newY); // Don't allow the FAB past the top of the parent
                newY = Math.min(parentHeight - viewHeight - layoutParams.bottomMargin, newY); // Don't allow the FAB past the bottom of the parent

                view.animate()
                        .x(newX)
                        .y(newY)
                        .setDuration(0)
                        .start();

                return true; // Consumed
            }
            case MotionEvent.ACTION_UP -> {
                float upRawX = motionEvent.getRawX();
                float upRawY = motionEvent.getRawY();

                float upDX = upRawX - downRawX;
                float upDY = upRawY - downRawY;

                if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                    return performClick();
                } else { // A drag
                    return true; // Consumed
                }
            }
            default -> {
                return super.onTouchEvent(motionEvent);
            }
        }
    }
}
