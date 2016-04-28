package com.tomorrowdev.spinrev.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.View;

/**
 * 
 * @author Gabriel Esteban
 *
 * Based on https://github.com/Todd-Davies/ProgressWheel
 * adapted for this project, having as a reference
 * - http://android-er.blogspot.com.es/2011/08/canvasdrawarc.html
 * - http://developer.android.com/reference/android/graphics/Canvas.html#drawArc(android.graphics.RectF, float, float, boolean, android.graphics.Paint)
 *
 */
public class SRProgressWheel extends View {

    //Sizes (with defaults)
    private int layout_height = 0;
    private int layout_width = 0;
    private float contourSize = 3;

    //Colors (with defaults)
    private int barColor = Color.argb(140, 0, 0, 0);
    private int contourColor = Color.argb(255, 0, 0, 0);
    private int rimColor = Color.argb(0, 255, 255, 255);

    //Paints
    private Paint barPaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint contourPaint = new Paint();

    //Rectangles
    private RectF circleBounds = new RectF();
    private RectF circleOuterContour = new RectF();

    int progress = 0;
    boolean isSpinning = false;
    
    public SRProgressWheel(Context context) {
        super(context);
    }
    
    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of the view,
     * because this method is called after measuring the dimensions of MATCH_PARENT & WRAP_CONTENT.
     * Use this dimensions to setup the bounds and paints.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Share the dimensions
        layout_width = w;
        layout_height = h;

        setupBounds();
        setupPaints();
        invalidate();
    }

    /**
     * Set the properties of the paints we're using to
     * draw the progress wheel
     */
    private void setupPaints() {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Style.FILL);

        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Style.FILL);

        contourPaint.setColor(contourColor);
        contourPaint.setAntiAlias(true);
        contourPaint.setStyle(Style.STROKE);
        contourPaint.setStrokeWidth(contourSize);
    }

    /**
     * Set the bounds of the component
     */
    private void setupBounds() {
        circleBounds = new RectF(0, 0, layout_width, layout_height);
        circleOuterContour = new RectF(contourSize/2, contourSize/2, layout_width - contourSize/2, layout_height - contourSize/2);
    }
    
    /**
     * Creates a circle, that is the background, then creates a contour and finally add
     * an arc depending on the progress
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2, rimPaint);
        //canvas.drawArc(circleOuterContour, 360, 360, false, contourPaint);
        canvas.drawArc(circleBounds, -90, progress, true, barPaint);
    }

    /**
     * Set the progress to a specific value
     * 
     * @param degrees the degrees to show
     */
    public void setProgress(int degrees) {
        progress = degrees;
        invalidate();
    }

    //----------------------------------
    //Getters + setters
    //----------------------------------

    public int getBarColor() {
		return barColor;
	}

	public void setBarColor(int barColor) {
		this.barColor = barColor;
	}

	public int getContourColor() {
		return contourColor;
	}

	public void setContourColor(int contourColor) {
		this.contourColor = contourColor;
	}

	public int getRimColor() {
		return rimColor;
	}

	public void setRimColor(int rimColor) {
		this.rimColor = rimColor;
	}
}