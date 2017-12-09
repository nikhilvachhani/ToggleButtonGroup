package com.nex3z.togglebuttongroup.button;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.nex3z.togglebuttongroup.R;

public class LabelToggle extends MarkerButton implements ToggleButton {
    private static final String LOG_TAG = LabelToggle.class.getSimpleName();

    private static final int DEFAULT_ANIMATION_DURATION = 150;

    private long mAnimationDuration = DEFAULT_ANIMATION_DURATION;
    private Animation mCheckAnimation;
    private Animation mUncheckAnimation;
    private ValueAnimator mTextColorAnimator;

    public LabelToggle(Context context) {
        this(context, null);
    }

    public LabelToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMarkerColorChanged() {
        initBackground();
    }

    private void init() {
        initBackground();
        initText();
        initAnimation();
    }

    private void initBackground() {
        GradientDrawable checked = new GradientDrawable();
        checked.setColor(mMarkerColor);
        checked.setCornerRadius(dpToPx(25));
        checked.setStroke(1, mMarkerColor);
        mIvBg.setImageDrawable(checked);

        GradientDrawable unchecked = new GradientDrawable();
        unchecked.setColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        unchecked.setCornerRadius(dpToPx(25));
        unchecked.setStroke((int) dpToPx(1), mMarkerColor);
        mTvText.setBackgroundDrawable(unchecked);
    }

    private void initText() {
        int padding = (int) dpToPx(16);
        mTvText.setPadding(padding, 0, padding, 0);
    }

    private void initAnimation() {
        final int defaultTextColor = getDefaultTextColor();
        final int checkedTextColor = getCheckedTextColor();

        mTextColorStateList.getDefaultColor();
        mTextColorAnimator = ValueAnimator.ofObject(
                new ArgbEvaluator(), defaultTextColor, checkedTextColor);
        mTextColorAnimator.setDuration(mAnimationDuration);
        mTextColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTvText.setTextColor((Integer)valueAnimator.getAnimatedValue());
            }
        });

        mCheckAnimation = new AlphaAnimation(0, 1);
        mCheckAnimation.setDuration(mAnimationDuration);
        mCheckAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mTvText.setTextColor(checkedTextColor);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        mUncheckAnimation = new AlphaAnimation(1, 0);
        mUncheckAnimation.setDuration(mAnimationDuration);
        mUncheckAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mIvBg.setVisibility(INVISIBLE);
                mTvText.setTextColor(defaultTextColor);}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (checked) {
            mIvBg.setVisibility(VISIBLE);
            mIvBg.startAnimation(mCheckAnimation);
            mTextColorAnimator.start();
        } else {
            mIvBg.setVisibility(VISIBLE);
            mIvBg.startAnimation(mUncheckAnimation);
            mTextColorAnimator.reverse();
        }
    }
}
