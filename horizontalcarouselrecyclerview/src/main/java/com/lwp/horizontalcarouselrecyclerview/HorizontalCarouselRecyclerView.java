package com.lwp.horizontalcarouselrecyclerview;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.getColor;


public class HorizontalCarouselRecyclerView extends RecyclerView {

    private Context context;
    private int activeColor;      // 激活颜色
    private int inactiveColor;    // 未激活颜色
    private List<Integer> viewsToChangeColor = new ArrayList<>();

    public HorizontalCarouselRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public HorizontalCarouselRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalCarouselRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs,
                                          int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
        activeColor = getColor(context, R.color.blue);
        inactiveColor = getColor(context, R.color.gray);
    }

    public void initialize(Adapter<?> adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, HORIZONTAL, false);
        adapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                post(new Runnable() {
                    @Override
                    public void run() {
                        int sidePadding = getWidth() / 2 - getChildAt(0).getWidth() / 2;
                        setPadding(sidePadding, 0, sidePadding, 0);
                        scrollToPosition(0);
                        addOnScrollListener(new OnScrollListener() {
                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                onScrollChanged();
                            }
                        });
                    }
                });
            }
        });
        setLayoutManager(layoutManager);
        setAdapter(adapter);
    }

    public void setViewsToChangeColor(List<Integer> viewIds) {
        this.viewsToChangeColor = viewIds;
    }

    private void onScrollChanged() {
        post(new Runnable() {
            @Override
            public void run() {
                int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    int childCenterX = (child.getLeft() + child.getRight()) / 2;
                    float scaleValue = getGaussianScale(childCenterX, 1f, 1f, 150);
//                    Log.d("ScaleValue", "scale value:" + scaleValue);
                    child.setScaleX(scaleValue);
                    child.setScaleY(scaleValue);
                    colorView(child, scaleValue);
                }
            }
        });
    }

    private void colorView(View child, float scaleValue) {
        float saturationPercent = (scaleValue - 1) / 1f;
        float alphaPercent = scaleValue / 2f;
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(saturationPercent);

        for (int viewId : viewsToChangeColor) {
            View view = child.findViewById(viewId);
            if (view instanceof ImageView) {
                ((ImageView) view).setColorFilter(new ColorMatrixColorFilter(matrix));
                ((ImageView) view).setImageAlpha((int) (255 * alphaPercent));
            } else if (view instanceof TextView) {
                int textColor = (int) new ArgbEvaluator().evaluate(saturationPercent, inactiveColor, activeColor);
                ((TextView) view).setTextColor(textColor);
            }
        }
    }


    private float getGaussianScale(int childCenterX, float minScaleOffset,
                                   float scaleFactor, double spreadFactor) {

        int recyclerCenterX = (getLeft() + getRight()) / 2;
        return (float) (Math.pow(Math.E,
                -Math.pow((double) (childCenterX - recyclerCenterX), 2)
                        / (2 * Math.pow(spreadFactor, 2))
        ) * scaleFactor + minScaleOffset);

    }

}
