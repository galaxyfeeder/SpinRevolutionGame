package com.tomorrowdev.spinrev.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class SRColorCircleView extends View{
	
	private Paint mPaint;
    private int width, height, color;

	public SRColorCircleView(Context context) {
		super(context);
		mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	}
	@Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        //radius is height/2 because we want the view to be a circle
        canvas.drawCircle(width / 2, height / 2, height / 2, mPaint);
    }
	public void changeColor(int color){
		mPaint.setColor(color);
		this.color = color;
		invalidate();
	}
	public int getColorWithlessAlpha(){
		return Color.argb(127, Color.red(color), Color.green(color), Color.blue(color));
	}
}