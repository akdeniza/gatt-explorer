package com.akdeniza.explorer.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerViewLine
 * @author Akdeniz on 10/01/2017.
 */

public class RecyclerViewLine extends RecyclerView.ItemDecoration {

    private static final int[] LISTDIVIDER = new int[]{android.R.attr.listDivider};

    private Drawable div;

    public RecyclerViewLine(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(LISTDIVIDER);
        div = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }


    public RecyclerViewLine(Context context, int resId) {
        div = ContextCompat.getDrawable(context, resId);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView view, RecyclerView.State state) {
        int left = view.getPaddingLeft();
                int right = view.getWidth() - view.getPaddingRight();
        int childCount = view.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = view.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + div.getIntrinsicHeight();
            div.setBounds(left, top, right, bottom);
            div.draw(c);
        }
    }
}