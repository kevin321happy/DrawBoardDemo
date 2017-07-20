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
    private boolean isAnimating=false;
    private boolean isShowing=false;

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
        //默认控件
        mDefaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit);
        mWidth = mDefaultBitmap.getWidth();
        mHeight = mDefaultBitmap.getHeight();
        LayoutParams params = new LayoutParams(mWidth, mHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //第一个子控件
        Bitmap first_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_paint);
        mFirst_view = new ImageView(context);
        mFirst_view.setImageBitmap(first_bitmap);
        mFirst_view.setVisibility(INVISIBLE);
        mFirst_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnChildMenuClickListener != null) {
                    mOnChildMenuClickListener.onChildClick(1);
//                    hideMenu();
                }
            }
        });

        addView(mFirst_view, params);

        //第二个子控件
        Bitmap scend_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_clear);
        mSecend_view = new ImageView(context);
        mSecend_view.setImageBitmap(scend_bitmap);
        mSecend_view.setVisibility(INVISIBLE);
        mSecend_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnChildMenuClickListener != null) {
                    mOnChildMenuClickListener.onChildClick(2);
//                    hideMenu();
                }


            }
        });
        addView(mSecend_view, params);

        //第三个子控件
        Bitmap third_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_out);
        mThird_view = new ImageView(context);
        mThird_view.setImageBitmap(third_bitmap);
        mThird_view.setVisibility(INVISIBLE);
        mThird_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnChildMenuClickListener != null) {
                    mOnChildMenuClickListener.onChildClick(3);
                    hideMenu();
                }
            }
        });
        addView(mThird_view, params);

        mDefault_view = new ImageView(context);
        mDefault_view.setImageBitmap(mDefaultBitmap);
        mDefault_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAnimating){
                    return;
                }
                if (!isShowing){
                    isShowing=true;
                    showMenu();
                }else {
                    isShowing=false;
                    hideMenu();
                }
            }
        });
        addView(mDefault_view,params);
    }

    //显示菜单
    private void showMenu() {
        mFirst_view.setVisibility(VISIBLE);
        mSecend_view.setVisibility(VISIBLE);
        mThird_view.setVisibility(VISIBLE);
        ObjectAnimator first_animator = ObjectAnimator.ofFloat(mFirst_view, "translationY", 0, -(mHeight + 80) * 3);
        ObjectAnimator secend_animator = ObjectAnimator.ofFloat(mSecend_view, "translationY", 0, -(mHeight + 80) * 2);
        ObjectAnimator third_animator = ObjectAnimator.ofFloat(mThird_view, "translationY", 0, -(mHeight + 80) * 1);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setInterpolator(new OvershootInterpolator());
        set.playTogether(first_animator,secend_animator,third_animator);
        set.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating=false;
            }
        });
        //动画开始执行
        set.start();
    }
    //隐藏菜单
    private void hideMenu() {
        ObjectAnimator first_animator = ObjectAnimator.ofFloat(mFirst_view, "translationY", mFirst_view.getTranslationY(), 0);
        ObjectAnimator secend_animator = ObjectAnimator.ofFloat(mSecend_view, "translationY",mSecend_view.getTranslationY(), 0);
        ObjectAnimator third_animator = ObjectAnimator.ofFloat(mThird_view, "translationY", mThird_view.getTranslationY(), 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setInterpolator(new OvershootInterpolator());
        set.playTogether(first_animator,secend_animator,third_animator);
        set.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating=false;
                mFirst_view.setVisibility(INVISIBLE);
                mSecend_view.setVisibility(INVISIBLE);
                mThird_view.setVisibility(INVISIBLE);
            }
        });
        //动画开始执行
        set.start();
    }

    public interface OnChildMenuClickListener {
        void onChildClick(int currentChild);
    }
}
