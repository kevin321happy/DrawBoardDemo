package com.example.kevin321vip.drawingboard.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.kevin321vip.drawingboard.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kevin321vip on 2017/7/19.
 * 弹出菜单控件
 */

public class PopUpView extends RelativeLayout {

    private Bitmap mDefaultBitmap;
    private int mWidth;
    private int mHeight;
    private ImageView mFirst_view;
    private ImageView mSecend_view;
    private ImageView mThird_view;
    private OnChildMenuClickListener mOnChildMenuClickListener;
    private ImageView mDefault_view;
    private boolean isAnimating = false;
    private boolean isShowing = false;
    /**
     * 存放菜单icon的集合
     */
    private List<Integer> mIcons = Arrays.asList(R.drawable.ic_paint, R.drawable.ic_box, R.drawable.ic_oval, R.drawable.ic_clear);
    private ImageView mFour_view;

    public void setOnChildMenuClickListener(OnChildMenuClickListener onChildMenuClickListener) {
        mOnChildMenuClickListener = onChildMenuClickListener;
    }

    public PopUpView(Context context) {
        this(context, null);
    }

    public PopUpView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopUpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //初始化方法
    private void init(Context context) {
        //先把编辑添加到底部
        mDefaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit);
        mWidth = mDefaultBitmap.getWidth();
        mHeight = mDefaultBitmap.getHeight();
        LayoutParams params = new LayoutParams(mWidth, mHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        if (mIcons != null && mIcons.size() > 0) {
            for (int i = 0; i < mIcons.size(); i++) {
                Integer icon = mIcons.get(i);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), icon);
                ImageView Icon = new ImageView(context);
                Icon.setImageBitmap(bitmap);
                Icon.setVisibility(INVISIBLE);
                addView(Icon, params);
                final int finalI = i;
                Icon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnChildMenuClickListener != null) {
                            mOnChildMenuClickListener.onChildClick(finalI);
                        }
                    }
                });
            }
        }

        mDefault_view = new ImageView(context);
        mDefault_view.setImageBitmap(mDefaultBitmap);
        mDefault_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAnimating) {
                    return;
                }
                if (!isShowing) {
                    isShowing = true;
                    showMenu();
                } else {
                    isShowing = false;
                    hideMenu();
                }
            }
        });
        addView(mDefault_view, params);
        mFirst_view = (ImageView) getChildAt(0);
        mSecend_view = (ImageView) getChildAt(1);
        mThird_view = (ImageView) getChildAt(2);
        mFour_view = (ImageView) getChildAt(3);
    }

    //显示菜单
    private void showMenu() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.setVisibility(VISIBLE);
        }
        ObjectAnimator first_animator = ObjectAnimator.ofFloat(mFirst_view, "translationY", 0, -(mHeight + 80) * 4);
        ObjectAnimator secend_animator = ObjectAnimator.ofFloat(mSecend_view, "translationY", 0, -(mHeight + 80) * 3);
        ObjectAnimator third_animator = ObjectAnimator.ofFloat(mThird_view, "translationY", 0, -(mHeight + 80) * 2);
        ObjectAnimator four_animator = ObjectAnimator.ofFloat(mFour_view, "translationY", 0, -(mHeight + 80) * 1);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setInterpolator(new OvershootInterpolator());
        set.playTogether(first_animator, secend_animator, third_animator, four_animator);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
            }
        });
        //动画开始执行
        set.start();
    }

    //隐藏菜单
    private void hideMenu() {
        ObjectAnimator first_animator = ObjectAnimator.ofFloat(mFirst_view, "translationY", mFirst_view.getTranslationY(), 0);
        ObjectAnimator secend_animator = ObjectAnimator.ofFloat(mSecend_view, "translationY", mSecend_view.getTranslationY(), 0);
        ObjectAnimator third_animator = ObjectAnimator.ofFloat(mThird_view, "translationY", mThird_view.getTranslationY(), 0);
        ObjectAnimator four_animator = ObjectAnimator.ofFloat(mFour_view, "translationY", mFour_view.getTranslationY(), 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setInterpolator(new OvershootInterpolator());
        set.playTogether(first_animator, secend_animator, third_animator, four_animator);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
                mFirst_view.setVisibility(INVISIBLE);
                mSecend_view.setVisibility(INVISIBLE);
                mThird_view.setVisibility(INVISIBLE);
                mFour_view.setVisibility(INVISIBLE);
            }
        });
        //动画开始执行
        set.start();
    }

    public interface OnChildMenuClickListener {
        void onChildClick(int currentChild);
    }
}
