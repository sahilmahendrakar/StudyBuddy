package com.studybuddy.sahilmahendrakar.studybuddy.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.studybuddy.sahilmahendrakar.studybuddy.R;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
//custom view to draw the main interface view in a day view
public class DayView extends View {
    private int ROWS = 24; //number of rows
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); //paint object to draw

    //constructor with attributes
    public DayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.GRAY);
    }
    //constructor without attributes
    public DayView(Context context){
        super(context, null);
        paint.setColor(Color.GRAY);
    }
    //draws 24 equally spaced lines to represent each hour in a day
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //calculates row height
        float rowHeight = getHeight() / (float) ROWS;
        for(int i = 0; i < ROWS; i++){
            float y = rowHeight*i; //calculates y value of line
            canvas.drawLine(0f, y, (float) getWidth(), y, paint); //draws line
        }
    }


}
