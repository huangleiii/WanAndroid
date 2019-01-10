package com.huanglei.wanandroid.widget.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.huanglei.wanandroid.utils.CommonUtils;

/**
 * Created by 黄垒 on 2019/1/8.
 */

public class FloatingActionButtonBehavior extends FloatingActionButton.Behavior{
    private boolean isAnimate=false;
    private boolean isShow=true;
    public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes== ViewCompat.SCROLL_AXIS_VERTICAL
        ||super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if(dy>0&&isShow&&!isAnimate){//手指上划隐藏
            ViewCompat.animate(child)
                    .translationY(350)
                    .setDuration(400)
                    .setInterpolator(new LinearOutSlowInInterpolator())
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                            isAnimate=true;
                            isShow=false;
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            isAnimate=false;
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                            isAnimate=false;
                        }
                    }).start();
        }else if(dy<0&&!isShow&&!isAnimate){//手指下滑恢复
            ViewCompat.animate(child)
                    .translationY(0)
                    .setDuration(400)
                    .setInterpolator(new LinearOutSlowInInterpolator())
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                            isAnimate=true;
                            isShow=true;
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            isAnimate=false;
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                            isAnimate=false;
                        }
                    });
        }
    }
}
