/**
 * Created on 24/09/2014 by Gabriel Esteban
 * Tomorrow Developers SCP, 2014
 */
package com.tomorrowdev.spinrev.views;

import com.tomorrowdev.spinrev.R;
import com.tomorrowdev.spinrev.SRUserData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Custom {@link: View} that shows the tutorial.
 * 
 * @author Gabriel Esteban
 *
 */
public class SRTutorial extends RelativeLayout{
	
	Context context = null;
	
	int OBJECT_WIDTH, OBJECT_HEIGHT;
	float DENSITY;
	
	private Resources res;

	public interface FinishListener{
		void onFinish();
	}
	
	private FinishListener mFinishListener; 
	
	public void setFinishListener(FinishListener mListener){
		mFinishListener = mListener;
	}
	
	/**
	 * After creating the object using this constructor, {@link: SRTutorial.init(Context)}
	 * should be called to instantiate all views with the correct size of the view.
	 * 
	 * @param context the application context
	 */
	public SRTutorial(Context context) {
		super(context);
		
		this.context = context;
		
		res = context.getResources();
	}
	
	/**
	 * Called by setSize() to instantiate all the objects related with the width and the height
	 */
	private void init(){
		final View fons_tut = new View(context);
    	RelativeLayout.LayoutParams fons_params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	fons_tut.setLayoutParams(fons_params);
    	fons_tut.setBackgroundColor(Color.rgb(255, 255, 255));
    	addView(fons_tut);
    	
    	final ImageView screenshot = new ImageView(context);
    	RelativeLayout.LayoutParams screenshot_params = new RelativeLayout.LayoutParams(OBJECT_WIDTH, (int)(OBJECT_HEIGHT*0.7));
    	screenshot_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	screenshot.setLayoutParams(screenshot_params);	    	
    	addView(screenshot);
    	
    	final ImageView spinner = new ImageView(context);
    	RelativeLayout.LayoutParams spinner_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, (int)(OBJECT_HEIGHT*0.25));
    	spinner_params.bottomMargin = (int) (OBJECT_HEIGHT*0.04);
    	spinner_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	spinner.setLayoutParams(spinner_params);
    	spinner.setImageResource(R.drawable.spinner);
    	addView(spinner);
    	
    	/*---------------BUTTONS------------------*/
    	
    	final ImageView back = new ImageView(context);
    	RelativeLayout.LayoutParams back_params = new RelativeLayout.LayoutParams((int)(OBJECT_WIDTH*0.2), (int)(OBJECT_WIDTH*0.2));
    	back_params.topMargin = (int)(OBJECT_HEIGHT*0.08);
    	back.setLayoutParams(back_params);
    	back.setImageResource(R.drawable.back);
    	addView(back);
    	
    	final ImageView next = new ImageView(context);
    	RelativeLayout.LayoutParams next_params = new RelativeLayout.LayoutParams((int)(OBJECT_WIDTH*0.2), (int)(OBJECT_WIDTH*0.2));
    	next_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	next_params.topMargin = (int)(OBJECT_HEIGHT*0.08);
    	next.setLayoutParams(next_params);
    	next.setImageResource(R.drawable.next);
    	addView(next);
    	
    	/*---------------ARROWS------------------*/
    	
    	final ImageView arrow1 = new ImageView(context);
    	RelativeLayout.LayoutParams arrow1_params = new RelativeLayout.LayoutParams((int)(OBJECT_WIDTH*0.1), (int)(OBJECT_WIDTH*0.1));
    	arrow1_params.topMargin = (int) (OBJECT_HEIGHT*0.68);
    	arrow1_params.leftMargin = (int) (OBJECT_WIDTH*0.5-OBJECT_WIDTH*0.05);
    	arrow1.setLayoutParams(arrow1_params);
    	arrow1.setImageResource(R.drawable.arrowup);
    	addView(arrow1);
    	
    	final ImageView arrow2 = new ImageView(context);
    	RelativeLayout.LayoutParams arrow2_params = new RelativeLayout.LayoutParams((int)(OBJECT_WIDTH*0.1), (int)(OBJECT_WIDTH*0.1));
    	arrow2_params.topMargin = (int) (OBJECT_HEIGHT*0.51);
    	arrow2_params.leftMargin = (int) (OBJECT_WIDTH*0.35-OBJECT_WIDTH*0.05);
    	arrow2.setLayoutParams(arrow2_params);
    	arrow2.setImageResource(R.drawable.arrowright);
    	addView(arrow2);
    	
    	final ImageView arrow3 = new ImageView(context);
    	RelativeLayout.LayoutParams arrow3_params = new RelativeLayout.LayoutParams((int)(OBJECT_WIDTH*0.1), (int)(OBJECT_WIDTH*0.1));
    	arrow3_params.topMargin = (int) (OBJECT_HEIGHT*0.91);
    	arrow3_params.leftMargin = (int) (OBJECT_WIDTH*0.78-OBJECT_WIDTH*0.05);
    	arrow3.setLayoutParams(arrow3_params);
    	arrow3.setImageResource(R.drawable.arrowleft);
    	addView(arrow3);
    	
    	/*---------------CERCLES------------------*/    	
    	
    	final ImageView cercle1 = new ImageView(context);
    	RelativeLayout.LayoutParams cercle1_params = new RelativeLayout.LayoutParams((int)(OBJECT_WIDTH*0.11), (int)(OBJECT_WIDTH*0.11));
    	cercle1_params.topMargin = (int) (OBJECT_HEIGHT*0.41);
    	cercle1_params.leftMargin = (int) (OBJECT_WIDTH*0.327-OBJECT_WIDTH*0.055);
    	cercle1.setLayoutParams(cercle1_params);
    	cercle1.setImageResource(R.drawable.pu_cercle);
    	addView(cercle1);
    	
    	final ImageView cercle2 = new ImageView(context);
    	RelativeLayout.LayoutParams cercle2_params = new RelativeLayout.LayoutParams((int)(OBJECT_WIDTH*0.11), (int)(OBJECT_WIDTH*0.11));
    	cercle2_params.topMargin = (int) (OBJECT_HEIGHT*0.41);
    	cercle2_params.leftMargin = (int) (OBJECT_WIDTH*0.548-OBJECT_WIDTH*0.055);
    	cercle2.setLayoutParams(cercle2_params);
    	cercle2.setImageResource(R.drawable.pu_cercle);
    	addView(cercle2);
    	
    	final ImageView cercle3 = new ImageView(context);
    	RelativeLayout.LayoutParams cercle3_params = new RelativeLayout.LayoutParams((int)(OBJECT_WIDTH*0.11), (int)(OBJECT_WIDTH*0.11));
    	cercle3_params.topMargin = (int) (OBJECT_HEIGHT*0.41);
    	cercle3_params.leftMargin = (int) (OBJECT_WIDTH*0.767-OBJECT_WIDTH*0.055);
    	cercle3.setLayoutParams(cercle3_params);
    	cercle3.setImageResource(R.drawable.pu_cercle);
    	addView(cercle3);
    	
    	/*---------------OTHER------------------*/
    	
    	final TextView text = new TextView(context);
    	RelativeLayout.LayoutParams text_params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)(OBJECT_HEIGHT*0.3));
    	text_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
    	text_params.setMargins((int)(OBJECT_WIDTH*0.2), (int)(OBJECT_HEIGHT*0.01), (int)(OBJECT_WIDTH*0.2), 0);
    	text.setLayoutParams(text_params);
    	text.setTextSize((int) (OBJECT_WIDTH/DENSITY * 0.07));
    	text.setTextColor(Color.BLACK);
    	text.setGravity(Gravity.CENTER);
    	addView(text);
    	
    	final ImageView hand = new ImageView(context);
    	RelativeLayout.LayoutParams hand_params = new RelativeLayout.LayoutParams((int)(OBJECT_HEIGHT*0.07), (int)(OBJECT_HEIGHT*0.1));
    	hand_params.bottomMargin = (int) (OBJECT_HEIGHT*0.08);
    	hand_params.leftMargin = (int) (OBJECT_WIDTH*0.47);
    	hand_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	hand.setLayoutParams(hand_params);
    	hand.setImageResource(R.drawable.hand);
    	addView(hand);
    	
    	final TextView combo = new TextView(context);
    	RelativeLayout.LayoutParams combo_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	combo_params.topMargin = (int) (OBJECT_HEIGHT*0.52);
    	combo_params.leftMargin = (int) (OBJECT_WIDTH*0.65);
    	combo.setLayoutParams(combo_params);
    	combo.setText("x2");
    	combo.setTextColor(Color.BLUE);
    	combo.setTextSize((int) (OBJECT_WIDTH/DENSITY * 0.07));
    	addView(combo);
    	
    	//First page settings
    	screenshot.setImageResource(R.drawable.screenshot_1);
		text.setText(res.getString(R.string.tutorial_1));
		back.setVisibility(View.INVISIBLE);
		next.setVisibility(View.VISIBLE);
		spinner.setVisibility(View.VISIBLE);
		hand.setVisibility(View.VISIBLE);
		arrow1.setVisibility(View.VISIBLE);
		arrow2.setVisibility(View.INVISIBLE);
		arrow3.setVisibility(View.INVISIBLE);
		cercle1.setVisibility(View.INVISIBLE);
		cercle2.setVisibility(View.INVISIBLE);
		cercle3.setVisibility(View.INVISIBLE);
		combo.setVisibility(View.INVISIBLE);
		spinner.startAnimation(new SpinnerAnimation());
		hand.startAnimation(new HandAnimation());
		arrow1.startAnimation(new ArrowYAnimation());
    	
    	OnTouchListener onTouch = new OnTouchListener() {
    		int step = 0;
    		int maxsteps = 4;
			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				
				switch (ev.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					if(v == back){
						back.setImageResource(R.drawable.back_pressed);
					}else if(v == next){
						next.setImageResource(R.drawable.next_pressed);
					}
					break;
				case MotionEvent.ACTION_UP:
					if(v == next){
						next.setImageResource(R.drawable.next);
						if(step < maxsteps){
							step++;
						}else{
							fons_tut.setVisibility(View.GONE);
							back.setVisibility(View.GONE);
							next.setVisibility(View.GONE);
							arrow1.setVisibility(View.GONE);
							arrow2.setVisibility(View.GONE);
							arrow3.setVisibility(View.GONE);
							cercle1.setVisibility(View.GONE);
							cercle2.setVisibility(View.GONE);
							cercle3.setVisibility(View.GONE);
							screenshot.setVisibility(View.GONE);
							text.setVisibility(View.GONE);
							combo.setVisibility(View.GONE);
							spinner.setVisibility(View.GONE);
							return false;
						}
					} else if(v == back){
						back.setImageResource(R.drawable.back);
						if(step > 0){
							step--;
						}
					}
					
					arrow1.clearAnimation();
					arrow2.clearAnimation();
					arrow3.clearAnimation();
					cercle1.clearAnimation();
					cercle2.clearAnimation();
					cercle3.clearAnimation();
					spinner.clearAnimation();
					hand.clearAnimation();
					
					switch (step) {
					case 0:
						screenshot.setImageResource(R.drawable.screenshot_1);
						text.setText(res.getString(R.string.tutorial_1));
						back.setVisibility(View.INVISIBLE);
						next.setVisibility(View.VISIBLE);
						spinner.setVisibility(View.VISIBLE);
						hand.setVisibility(View.VISIBLE);
						arrow1.setVisibility(View.VISIBLE);
						arrow2.setVisibility(View.INVISIBLE);
						arrow3.setVisibility(View.INVISIBLE);
						cercle1.setVisibility(View.INVISIBLE);
						cercle2.setVisibility(View.INVISIBLE);
						cercle3.setVisibility(View.INVISIBLE);
						combo.setVisibility(View.INVISIBLE);
						spinner.startAnimation(new SpinnerAnimation());
						hand.startAnimation(new HandAnimation());
						arrow1.startAnimation(new ArrowYAnimation());
						break;
					case 1:
						screenshot.setImageResource(R.drawable.screenshot_1);
						text.setText(res.getString(R.string.tutorial_2));
						back.setVisibility(View.VISIBLE);
						next.setVisibility(View.VISIBLE);
						spinner.setVisibility(View.VISIBLE);
						hand.setVisibility(View.VISIBLE);
						arrow1.setVisibility(View.INVISIBLE);
						arrow2.setVisibility(View.VISIBLE);
						arrow3.setVisibility(View.INVISIBLE);
						cercle1.setVisibility(View.INVISIBLE);
						cercle2.setVisibility(View.INVISIBLE);
						cercle3.setVisibility(View.INVISIBLE);
						combo.setVisibility(View.VISIBLE);
						spinner.startAnimation(new SpinnerAnimation());
						hand.startAnimation(new HandAnimation());
						arrow2.startAnimation(new ArrowXAnimation());
						break;
					case 2:
						screenshot.setImageResource(R.drawable.screenshot_1);
						text.setText(res.getString(R.string.tutorial_3));
						back.setVisibility(View.VISIBLE);
						next.setVisibility(View.VISIBLE);
						spinner.setVisibility(View.VISIBLE);
						hand.setVisibility(View.INVISIBLE);
						arrow1.setVisibility(View.INVISIBLE);
						arrow2.setVisibility(View.INVISIBLE);
						arrow3.setVisibility(View.INVISIBLE);
						cercle1.setVisibility(View.VISIBLE);
						cercle2.setVisibility(View.VISIBLE);
						cercle3.setVisibility(View.VISIBLE);
						combo.setVisibility(View.INVISIBLE);
						cercle1.startAnimation(new CircleAnimation());
						cercle2.startAnimation(new CircleAnimation());
						cercle3.startAnimation(new CircleAnimation());
						break;
					case 3:
						screenshot.setImageResource(R.drawable.screenshot_2);
						text.setText(res.getString(R.string.tutorial_4));
						back.setVisibility(View.VISIBLE);
						next.setVisibility(View.VISIBLE);
						spinner.setVisibility(View.INVISIBLE);
						hand.setVisibility(View.INVISIBLE);
						arrow1.setVisibility(View.INVISIBLE);
						arrow2.setVisibility(View.INVISIBLE);
						arrow3.setVisibility(View.VISIBLE);
						cercle1.setVisibility(View.VISIBLE);
						cercle2.setVisibility(View.VISIBLE);
						cercle3.setVisibility(View.VISIBLE);
						combo.setVisibility(View.INVISIBLE);
						cercle1.startAnimation(new CircleAnimation());
						cercle2.startAnimation(new CircleAnimation());
						cercle3.startAnimation(new CircleAnimation());
						arrow3.startAnimation(new ArrowXAnimation());
						break;
					case 4:
						mFinishListener.onFinish();
						break;
					default:break;
					}
					break;

				default:break;
				}
								
				
				return true;
			}
		};
		back.setOnTouchListener(onTouch);
		next.setOnTouchListener(onTouch);
		
		SRUserData.getInstace().setFirstLoad(false);
	}
	
	/**
	 * Called to pass width, height and density to this class.
	 * 
	 * @param width the object width
	 * @param height the object height
	 * @param density the screen density
	 */
	public void setSize(int width, int height, float density){
		OBJECT_WIDTH = width;
		OBJECT_HEIGHT = height;

		DENSITY = density;
		
		init();
	}
	
	/**
	 * Custom animation for rotating the spinner.
	 * 
	 * @author Gabriel Esteban
	 */
	private class SpinnerAnimation extends Animation {

		float pivotX, pivotY;
		
		/**
		 * Public constructor for this class
		 */
		public SpinnerAnimation() {
			setDuration(3000);
			setInterpolator(new LinearInterpolator());
			setRepeatCount(Animation.INFINITE);
		}
		
		@Override
		public void initialize(int width, int height, int parentWidth, int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			
			pivotX = resolveSize(RELATIVE_TO_SELF, 0.5f, width, parentWidth);
			pivotY = resolveSize(RELATIVE_TO_SELF, 0.5f, height, parentHeight);
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			
			float degrees = 360 * interpolatedTime;
			
			t.getMatrix().setRotate(degrees, pivotX, pivotY);
		}
	}
	
	/**
	 * Custom animation for rotating the spinner.
	 * 
	 * @author Gabriel Esteban
	 */
	private class HandAnimation extends Animation {

		float oX, oY;
		float spinnerWidth;
		
		/**
		 * Public constructor for this class
		 * 
		 * Following http://gamedev.stackexchange.com/questions/9607/moving-an-object-in-a-circular-path
		 */
		public HandAnimation() {
			setDuration(3000);
			setInterpolator(new LinearInterpolator());
			setRepeatCount(Animation.INFINITE);
			
			spinnerWidth = OBJECT_HEIGHT*0.25f;
		}
		
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			
			float size = (spinnerWidth/2)*0.8f;
			float angle = (float) (2 * Math.PI * interpolatedTime);
						
			float aX = (float) (oX + Math.cos(angle-Math.PI*0.3)*size);
			float aY = (float) (oY + Math.sin(angle-Math.PI*0.3)*size);
			
			t.getMatrix().setTranslate(aX, aY);
		}
		
		@Override
		public void initialize(int width, int height, int parentWidth, int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			
			oX = resolveSize(ABSOLUTE, 0f, width, parentWidth);
			oY = resolveSize(ABSOLUTE, 0.9f, height, parentHeight);
		}
	}
	
	/**
	 * Custom animation for scaling and de-scaling the circles.
	 * 
	 * @author Gabriel Esteban
	 */
	private class CircleAnimation extends Animation {

		float pivotX, pivotY;
		
		/**
		 * Public constructor for this class
		 */
		public CircleAnimation() {
			setDuration(1000);
			setInterpolator(new LinearInterpolator());
			setRepeatCount(Animation.INFINITE);
		}
		
		@Override
		public void initialize(int width, int height, int parentWidth, int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			
			pivotX = resolveSize(RELATIVE_TO_SELF, 0.5f, width, parentWidth);
			pivotY = resolveSize(RELATIVE_TO_SELF, 0.5f, height, parentHeight);
		}

		@SuppressLint("NewApi")
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			
			float s = 1.0f;
			if(interpolatedTime > 0.5){
				s = 1.3f - (1.3f - 0.9f)*interpolatedTime;
			}else{
				s = 0.9f + (1.3f - 0.9f)*interpolatedTime;
			}
			
			if(Build.VERSION.SDK_INT >= 11){
				float scale = getScaleFactor();
				t.getMatrix().setScale(s, s, scale * pivotX, scale * pivotY);
			}else{
				t.getMatrix().setScale(s, s, pivotX, pivotY);
			}
		}
	}
	
	/**
	 * Custom animation for moving right-left some arrows.
	 * 
	 * @author Gabriel Esteban
	 */
	private class ArrowXAnimation extends Animation {

		float start, end;
		
		/**
		 * Public constructor for this class
		 */
		public ArrowXAnimation() {
			setDuration(1000);
			setInterpolator(new LinearInterpolator());
			setRepeatCount(Animation.INFINITE);
		}
		
		@Override
		public void initialize(int width, int height, int parentWidth, int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			
			start = resolveSize(RELATIVE_TO_SELF, -0.2f, width, parentWidth);
			end = resolveSize(RELATIVE_TO_SELF, 0.3f, width, parentWidth);
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			
			float x = start;
			if(interpolatedTime > 0.5){
				x = start - (start - end)*interpolatedTime;
			}else{
				x = end + (start - end)*interpolatedTime;
			}
			
			t.getMatrix().setTranslate(x, 0);
		}
	}
	
	/**
	 * Custom animation for moving right-left some arrows.
	 * 
	 * @author Gabriel Esteban
	 */
	private class ArrowYAnimation extends Animation {

		float start, end;
		
		/**
		 * Public constructor for this class
		 */
		public ArrowYAnimation() {
			setDuration(1000);
			setInterpolator(new LinearInterpolator());
			setRepeatCount(Animation.INFINITE);
		}
		
		@Override
		public void initialize(int width, int height, int parentWidth, int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			
			start = resolveSize(RELATIVE_TO_SELF, -0.5f, height, parentHeight);
			end = resolveSize(RELATIVE_TO_SELF, 0f, height, parentHeight);
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			
			float y = start;
			if(interpolatedTime > 0.5){
				y = start - (start - end)*interpolatedTime;
			}else{
				y = end + (start - end)*interpolatedTime;
			}
			
			t.getMatrix().setTranslate(0, y);
		}
	}
}
