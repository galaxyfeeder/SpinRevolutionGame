package com.tomorrowdev.spinrev.views;

import com.tomorrowdev.spinrev.R;
import com.tomorrowdev.spinrev.SRUserData;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SRPointsMenu extends RelativeLayout implements OnTouchListener{
	OnIAEventListener mIAListener;	
	
	public interface OnIAEventListener{
		public void onEvent(int type);
	}
	
	public void setIAEventListener(OnIAEventListener event){
		mIAListener = event;	
	}
	
	public final static int POINTS_20_BUTTON = 10;
	public final static int POINTS_15_BUTTON = 11;
	public final static int POINTS_50_BUTTON = 12;
	public final static int POINTS_100_BUTTON = 13;
	
	public final static String POINTS_20_BUTTON_SKU = "com.tomorrowdev.com.spinrev.points20";
	public final static String POINTS_50_BUTTON_SKU = "com.tomorrowdev.com.spinrev.points50";
	public final static String POINTS_100_BUTTON_SKU = "com.tomorrowdev.com.spinrev.points100";
	
	int OBJECT_WIDTH, OBJECT_HEIGHT;
	float DENSITY;
	
	TextView menuTitle, menuInfo;
	ImageView points20, points15, points50, points100;
	LinearLayout table;
	LinearLayout row1, row2;
	Resources res;
	
	Context context;
	
	public SRPointsMenu(Context context) {
		super(context);
		this.context = context;
		res = context.getResources();
	}
	
	public void setSize(int width, int height, float density){
		OBJECT_WIDTH = width;
		OBJECT_HEIGHT = height;

		DENSITY = density;
		
		sharedConstructor(context);
	}

	private void sharedConstructor(Context context) {
		setVisibility(View.INVISIBLE);
		setBackgroundResource(R.drawable.iamenu);
		
		//Typefaces
	    Typeface Helveticaneue_light = Typeface.createFromAsset(context.getAssets(), "fonts/Helveticaneue-light.ttf");
	    Typeface Helveticaneue_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Helveticaneue-bold.ttf");
		
	    //linearLayout container
	  	LinearLayout container = new LinearLayout(context);
	  	LinearLayout.LayoutParams container_params = new LinearLayout.LayoutParams(OBJECT_WIDTH, OBJECT_HEIGHT);
	  	container.setLayoutParams(container_params);
	  	container.setOrientation(LinearLayout.VERTICAL);
	  	container.setGravity(Gravity.CENTER);
	  	container.setWeightSum(100);
	  	addView(container);
	    
		//menuTitle
		menuTitle = new TextView(context);
		LinearLayout.LayoutParams menuTitle_params = new LinearLayout.LayoutParams(OBJECT_WIDTH/10*7, 0);
		menuTitle_params.weight = 15;
		menuTitle.setLayoutParams(menuTitle_params);
		menuTitle.setTextSize((int)(OBJECT_WIDTH/DENSITY*0.09));
		menuTitle.setGravity(Gravity.CENTER);
		menuTitle.setTextColor(Color.BLACK);
		menuTitle.setTypeface(Helveticaneue_bold);
		menuTitle.setText(res.getString(R.string.points_title));		
		container.addView(menuTitle);
		
		//menuInfo		
		menuInfo = new TextView(context);
		LinearLayout.LayoutParams menuInfo_params = new LinearLayout.LayoutParams(OBJECT_WIDTH/10*8, 0);
		menuInfo_params.weight = 20;
		menuInfo.setLayoutParams(menuInfo_params);
		menuInfo.setTextSize((int)(OBJECT_WIDTH/DENSITY*0.07));
		menuInfo.setTextColor(Color.BLACK);
		menuInfo.setGravity(Gravity.CENTER);
		menuInfo.setTypeface(Helveticaneue_light);
		menuInfo.setText(res.getString(R.string.points_description));
		container.addView(menuInfo);
		
		//table that contains buttons to gain points
		table = new LinearLayout(context);
		LinearLayout.LayoutParams table_params = new LinearLayout.LayoutParams(OBJECT_WIDTH/10*8, 0);
		table_params.weight = 55;
		table.setLayoutParams(table_params);
		table.setOrientation(LinearLayout.VERTICAL);
		table.setWeightSum(100);
		container.addView(table);
		
		int buttonSide = (int) (OBJECT_WIDTH*0.25);
		
		//row 1, 20 points & 15 points
		row1 = new LinearLayout(context);
		LinearLayout.LayoutParams row1_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
		row1_params.weight = 50;
		row1.setLayoutParams(row1_params);
		row1.setOrientation(LinearLayout.HORIZONTAL);
		row1.setGravity(Gravity.CENTER);
		table.addView(row1);
		
		//20 points
		points20 = new ImageView(context);
		LinearLayout.LayoutParams points20_params = new LinearLayout.LayoutParams(buttonSide, buttonSide);
		points20.setLayoutParams(points20_params);
		points20.setImageResource(R.drawable.points20);
		points20.setOnTouchListener(this);
	    row1.addView(points20);
	    
	    //15 points
	  	points15 = new ImageView(context);
	  	LinearLayout.LayoutParams points15_params = new LinearLayout.LayoutParams(buttonSide, buttonSide);
	  	points15_params.setMargins(10, 0, 0, 0);
	  	points15.setLayoutParams(points15_params);
	  	points15.setImageResource(R.drawable.points15);
	  	points15.setOnTouchListener(this);
	  	//Enquesta eliminada
	  	//row1.addView(points15);
		
		//row 2, 50 points & 100 points
	  	row2 = new LinearLayout(context);
		LinearLayout.LayoutParams row2_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
		row2_params.weight = 50;
		row2.setLayoutParams(row2_params);
		row2.setOrientation(LinearLayout.HORIZONTAL);
		row2.setGravity(Gravity.CENTER);
		table.addView(row2);
		
		//50 points
		points50 = new ImageView(context);
		LinearLayout.LayoutParams points50_params = new LinearLayout.LayoutParams(buttonSide, buttonSide);
		points50.setLayoutParams(points50_params);
		points50.setImageResource(R.drawable.points50);
		points50.setOnTouchListener(this);
		row2.addView(points50);
		
		//100 points
		points100 = new ImageView(context);
		LinearLayout.LayoutParams points100_params = new LinearLayout.LayoutParams(buttonSide, buttonSide);
		points100_params.setMargins(10, 0, 0, 0);
		points100.setLayoutParams(points100_params);
		points100.setImageResource(R.drawable.points100);
		points100.setOnTouchListener(this);
		row2.addView(points100);
		
		/*if(Build.VERSION.SDK_INT < 14 || SRUserData.getInstace().isSurveyDone()){
			row1.removeView(points15);
		}*/
	}
	
	public void openPointsMenu(){
		setVisibility(View.VISIBLE);
	}
	
	public void closePointsMenu(){
		setVisibility(View.GONE);
	}

	Rect rect;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
			if(v == points20){				
				points20.setImageResource(R.drawable.points20_pressed);
			}else if(v == points15){
				points15.setImageResource(R.drawable.points15_pressed);
			}else if(v == points50){
				points50.setImageResource(R.drawable.points50_pressed);
			}else if(v == points100){
				points100.setImageResource(R.drawable.points100_pressed);
			}
			break;
		case MotionEvent.ACTION_UP:
			Boolean isOn = true;
			if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                isOn = false;
            }
			if(v == points20){
				if(isOn){
					mIAListener.onEvent(POINTS_20_BUTTON);
				}
				points20.setImageResource(R.drawable.points20);
			}else if(v == points15){
				if(isOn){
					mIAListener.onEvent(POINTS_15_BUTTON);
				}
				points15.setImageResource(R.drawable.points15);
			}else if(v == points50){
				if(isOn){
					mIAListener.onEvent(POINTS_50_BUTTON);
				}
				points50.setImageResource(R.drawable.points50);
			}else if(v == points100){
				if(isOn){
					mIAListener.onEvent(POINTS_100_BUTTON);
				}
				points100.setImageResource(R.drawable.points100);
			}
			break;
		default:
			break;
		}
		return true;
	}
}