package com.example.jojo.recyclescan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.GridView;

public class MyGridView extends GridView {

    private Bitmap background;
    private int mShelfWidth;
    private int mShelfHeight;

    public MyGridView(Context context, AttributeSet attributes) {
        super(context, attributes);

        final Bitmap shelfBackground = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.spacebottomshelf);
        setBackground(shelfBackground);
    }

    public void setBackground(Bitmap background) {
        this.background = background;

        mShelfWidth = background.getWidth();
        mShelfHeight = background.getHeight();
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        final int count = getChildCount();
        final int top = count > 0 ? getChildAt(0).getTop() : 0;
        final int shelfWidth = mShelfWidth;
        final int shelfHeight = mShelfHeight;
        final int width = getWidth();
        final int height = getHeight();
        final Bitmap background = this.background;

        for (int x = 0; x < width; x += shelfWidth) {
            for (int y = top; y < height; y += shelfHeight) {
                canvas.drawBitmap(background, x, y, null);
            }

            //This draws the top pixels of the shelf above the current one

            Rect source = new Rect(0, mShelfHeight - top, mShelfWidth, mShelfHeight);
            Rect dest = new Rect(x, 0, x + mShelfWidth, top );

            canvas.drawBitmap(background, source, dest, null);
        }


        super.dispatchDraw(canvas);
    }
}
