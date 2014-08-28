/*
 * Copyright 2014 Basit Parkar.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @date 28/8/14 2:51 PM
 * @modified 12/6/14 5:26 PM
 */

package com.mobility.iz.parsedemos.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mobility.iz.parsedemos.R;


/**
 * Layout which an {@link android.widget.TextView} to show a floating label when the hint is hidden
 * due to the user inputting text.
 *
 * @see <a href="https://dribbble.com/shots/1254439--GIF-Mobile-Form-Interaction">Matt D. Smith on Dribble</a>
 * @see <a href="http://bradfrostweb.com/blog/post/float-label-pattern/">Brad Frost's blog post</a>
 */

/**
 * Created by Basit Parkar on 5/21/2014.
 */
public final class FloatingLabelButton extends FrameLayout {

    private static final long ANIMATION_DURATION = 150;

    private static final float DEFAULT_PADDING_LEFT_RIGHT_DP = 12f;

    private Button mTextView;
    private TextView mLabel;

    public FloatingLabelButton(Context context) {
        this(context, null);
    }

    public FloatingLabelButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingLabelButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray a = context
                .obtainStyledAttributes(attrs, R.styleable.FloatLabelLayout);

        final int sidePadding = a.getDimensionPixelSize(
                R.styleable.FloatLabelLayout_floatLabelSidePadding,
                dipsToPix(DEFAULT_PADDING_LEFT_RIGHT_DP));
        mLabel = new TextView(context);
        mLabel.setPadding(sidePadding, 0, sidePadding, 0);
        mLabel.setVisibility(INVISIBLE);

        mLabel.setTextAppearance(context,
                a.getResourceId(R.styleable.FloatLabelLayout_floatLabelTextAppearance,
                        android.R.style.TextAppearance_Small)
        );

        addView(mLabel, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        a.recycle();
    }

    @Override
    public final void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof Button) {
            // If we already have an TextView, throw an exception
            if (mTextView != null) {
                throw new IllegalArgumentException("We already have an TextView, can only have one");
            }

            // Update the layout params so that the TextView is at the bottom, with enough top
            // margin to show the label
            final LayoutParams lp = new LayoutParams(params);
            lp.gravity = Gravity.BOTTOM;
            lp.topMargin = (int) mLabel.getTextSize();
            params = lp;

            setTextView((Button) child);
        }

        // Carry on adding the View...
        super.addView(child, index, params);
    }

    /**
     * @return the {@link android.widget.TextView} text input
     */
    public Button getTextView() {
        return mTextView;
    }

    private void setTextView(Button textView) {
        mTextView = textView;

        // Add a TextWatcher so that we know when the text input has changed
        mTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    // The text is empty, so hide the label if it is visible
                    if (mLabel.getVisibility() == View.VISIBLE) {
                        hideLabel();
                    }
                } else {
                    // The text is not empty, so show the label if it is not visible
                    if (mLabel.getVisibility() != View.VISIBLE) {
                        showLabel();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });

        mLabel.setText(mTextView.getHint());
    }

    /**
     * @return the {@link android.widget.TextView} label
     */
    public TextView getLabel() {
        return mLabel;
    }

    /**
     * Show the label using an animation
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void showLabel() {
        mLabel.setVisibility(View.VISIBLE);
        mLabel.setAlpha(0f);
        mLabel.setTranslationY(mLabel.getHeight());
        mLabel.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(ANIMATION_DURATION)
                .setListener(null).start();


       /* AnimationSet animationSet = new AnimationSet(true);

        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0,
                Animation.ABSOLUTE, mLabel.getHeight(), Animation.ABSOLUTE, 0);
        translateAnimation.setDuration(300);
        animationSet.addAnimation(translateAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(300);
        animationSet.addAnimation(alphaAnimation);

        mLabel.startAnimation(animationSet);*/
    }

    /**
     * Hide the label using an animation
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void hideLabel() {
        mLabel.setAlpha(1f);
        mLabel.setTranslationY(0f);
        mLabel.animate()
                .alpha(0f)
                .translationY(mLabel.getHeight())
                .setDuration(ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLabel.setVisibility(View.GONE);
                    }
                }).start();

        /*AnimationSet animationSet = new AnimationSet(true);

        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0,
                Animation.ABSOLUTE,0, Animation.ABSOLUTE,mLabel.getHeight());
        translateAnimation.setDuration(300);
        animationSet.addAnimation(translateAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(300);
        animationSet.addAnimation(alphaAnimation);


        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLabel.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        mLabel.startAnimation(animationSet);*/
    }

    /**
     * Helper method to convert dips to pixels.
     */
    private int dipsToPix(float dps) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps,
                getResources().getDisplayMetrics());
    }
}