package com.example.jojo.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Eigene ListView, damit ListView nur so hoch ist, wie nötig.
 * Ansonsten, wenn nur height: wrap_content, wurde nur das erste Item angezeigt und die anderen Items befinden sich
 * unsichtbar als ScrollView darunter.
 * (Siehe Methode OnMeasure)
 */
public class MyListView extends ListView {

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}