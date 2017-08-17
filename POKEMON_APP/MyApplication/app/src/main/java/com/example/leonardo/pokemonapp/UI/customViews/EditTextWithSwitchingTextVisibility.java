package com.example.leonardo.pokemonapp.UI.customViews;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.leonardo.pokemonapp.R;

/**
 * Created by leonardo on 05/08/17.
 */

public class EditTextWithSwitchingTextVisibility extends AppCompatEditText {

    private Drawable drawableRight;
    private int drawableId;
    private OnDrawableClickedListener listener;

    public EditTextWithSwitchingTextVisibility(Context context) {
        super(context);
        init();
    }

    public EditTextWithSwitchingTextVisibility(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithSwitchingTextVisibility(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(OnDrawableClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        drawableRight = end;
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            int actionX = (int) event.getX();
            int actionY = (int) event.getY();

            actionX = getWidth() - actionX;

            Rect drawableRect = drawableRight.getBounds();
            if(onDrawableClick(drawableRect, actionX, actionY)) {
                if(drawableId == R.drawable.ic_visibility_off) {
                    switchViewBehaviour(R.drawable.ic_visibility_on);
                } else {
                    switchViewBehaviour(R.drawable.ic_visibility_off);
                }

                event.setAction(MotionEvent.ACTION_CANCEL);
                return true;
            }

            return super.onTouchEvent(event);
        }

        return super.onTouchEvent(event);
    }

    private boolean onDrawableClick(Rect drawableRect, int actionX, int actionY) {
        int acceptableX = drawableRect.width();
        int acceptableY = drawableRect.height();

        if(actionX < acceptableX + 40 && actionX > - 40  && actionY <  acceptableY + 40 && actionY > - 40) {
            return true;
        }

        return false;
    }

    @Override
    protected void finalize() throws Throwable {
        drawableRight = null;
        super.finalize();
    }

    private void switchViewBehaviour(int drawable) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), drawable), null);
        drawableId = drawable;

        listener.onPasswordVissibilityChanged(drawable);

        switch(drawable) {
            case R.drawable.ic_visibility_off:
                setTransformationMethod(new NoTransformationMethod());
                return;
            case R.drawable.ic_visibility_on:
                setTransformationMethod(new PasswordTransformationMethod());
                return;
        }
    }

    private void init() {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_visibility_on);
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
        drawableId = R.drawable.ic_visibility_on;
        setTransformationMethod(new PasswordTransformationMethod());
    }

    public void setPasswordVisibilityDrawable(int passwordVisibilityDrawable) {
        switchViewBehaviour(passwordVisibilityDrawable);
    }

    private static class NoTransformationMethod implements TransformationMethod {

        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }

        @Override
        public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {}

    }

    public interface OnDrawableClickedListener {

        void onPasswordVissibilityChanged(int drawableId);

    }
}
