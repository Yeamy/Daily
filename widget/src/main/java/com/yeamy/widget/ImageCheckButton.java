package com.yeamy.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.widget.Checkable;
import android.widget.ImageView;

public class ImageCheckButton extends ImageView implements Checkable {
    private boolean checked;
    private OnCheckedChangeListener listener;
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    public ImageCheckButton(Context context) {
        this(context, null);
    }

    public ImageCheckButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageCheckButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public ImageCheckButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.ImageCheckButton, defStyleAttr, defStyleRes);
        boolean checked = a.getBoolean(R.styleable.ImageCheckButton_checked, false);
        a.recycle();
        setChecked(checked);
        setFocusable(true);
        setClickable(true);
    }

    @Override
    public boolean performClick() {
        toggle();

        if (listener != null) {
            listener.onCheckedChanged(ImageCheckButton.this, isChecked());
        }

        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }
        return handled;
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(ImageCheckButton imageView, boolean isChecked);
    }

}