package com.tomorrowdev.spinrev.views;

import com.tomorrowdev.spinrev.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnTouchListener;

public class SRPUHeader extends RelativeLayout implements OnTouchListener{
	OnExtraPUEventListener mExtraListener;
	OnComboPUEventListener mComboListener;
	OnImmuPUEventListener mImmuListener;
	
	public interface OnExtraPUEventListener{
		public void onEvent();
	}
	
	public interface OnComboPUEventListener{
		public void onEvent();
	}
	
	public interface OnImmuPUEventListener{
		public void onEvent();
	}
	
	public void setExtraPUEventListener(OnExtraPUEventListener lifeEvent){
		mExtraListener = lifeEvent;	
	}
	
	public void setComboPUEventListener(OnComboPUEventListener comboEvent){
		mComboListener = comboEvent;	
	}
	
	public void setImmuPUEventListener(OnImmuPUEventListener immuEvent){
		mImmuListener = immuEvent;	
	}

	int OBJECT_WIDTH, OBJECT_HEIGHT;
	float DENSITY;

	ImageView extraPUbutton, comboPUbutton, immuPUbutton;
	TextView extraPUtext, comboPUtext, immuPUtext;
	View rect1, rect2;
	SRProgressWheel immuProgress, comboProgress;
	
	Context context;
	
	public SRPUHeader(Context context) {
		super(context);
		this.context = context;
	}
	
	public void setSize(int width, int height, float density){
		OBJECT_WIDTH = width;
		OBJECT_HEIGHT = height;

		DENSITY = density;
		
		sharedConstructor(context);
	}
	
	private void sharedConstructor(Context context) {
		Typeface Helveticaneue_light = Typeface.createFromAsset(context.getAssets(), "fonts/Helveticaneue-light.ttf");
		
		int buttonSide = (int)(0.7 * OBJECT_HEIGHT);
		int textSize = (int) (OBJECT_WIDTH/DENSITY * 0.1);
		int textWidth = (int)(OBJECT_WIDTH*0.24-buttonSide/2);
		int PUbuttonsY = (int)(0.5 * OBJECT_HEIGHT)-buttonSide/2;
		int PUtextY = PUbuttonsY;
		
		//background color
		setBackgroundColor(Color.rgb(30, 225, 30));
		
		//combo PU Label
	  	comboPUtext = new TextView(context);
	  	comboPUtext.setText("0");
	  	RelativeLayout.LayoutParams comboPUtext_params = new RelativeLayout.LayoutParams(textWidth, LayoutParams.WRAP_CONTENT);
	  	comboPUtext_params.setMargins(0, PUtextY, 0, 0);
	  	comboPUtext.setLayoutParams(comboPUtext_params);
	  	comboPUtext.setTextColor(Color.WHITE);
	  	comboPUtext.setTextSize(textSize);
	    comboPUtext.setGravity(Gravity.CENTER);
	    comboPUtext.setTypeface(Helveticaneue_light);
	    addView(comboPUtext);	
		
		//combo PU Button
	  	comboPUbutton = new ImageView(context);
	  	comboPUbutton.setImageResource(R.drawable.combo_powerup);
	  	RelativeLayout.LayoutParams comboPUbutton_params = new LayoutParams(buttonSide, buttonSide);
	  	comboPUbutton_params.setMargins((int)(OBJECT_WIDTH*0.24-buttonSide/2), PUbuttonsY, 0, 0);
	  	comboPUbutton.setLayoutParams(comboPUbutton_params);
	  	comboPUbutton.setOnTouchListener(this);
	  	addView(comboPUbutton);
	  	
	  	//combo progress bar(circular)
	  	comboProgress = new SRProgressWheel(context);
	  	comboProgress.setLayoutParams(comboPUbutton_params);
	  	comboProgress.setVisibility(View.GONE);
	  	addView(comboProgress);
		
	    //rect
	    rect1 = new View(context);
	    RelativeLayout.LayoutParams rect1_params = new RelativeLayout.LayoutParams((int)(OBJECT_WIDTH*0.007),(int)(OBJECT_HEIGHT*0.8));
	    rect1_params.setMargins((int)(OBJECT_WIDTH*0.333), (int)(OBJECT_HEIGHT*0.1), 0, (int)(OBJECT_HEIGHT*0.1));
	    rect1.setLayoutParams(rect1_params);
	    rect1.setBackgroundColor(Color.WHITE);
	    addView(rect1);
	    
	    //Extra PU Label
	    extraPUtext = new TextView(context);
		extraPUtext.setText("0");
	    RelativeLayout.LayoutParams extraPUtext_params = new RelativeLayout.LayoutParams(textWidth, LayoutParams.WRAP_CONTENT);
	    extraPUtext_params.setMargins((int)(OBJECT_WIDTH*0.333), PUtextY, 0, 0);
	    extraPUtext.setLayoutParams(extraPUtext_params);
	    extraPUtext.setTextColor(Color.WHITE);
	    extraPUtext.setTextSize(textSize);
	    extraPUtext.setGravity(Gravity.CENTER);
	    extraPUtext.setTypeface(Helveticaneue_light);
	    addView(extraPUtext);
	    
		//Extra PU Button
		extraPUbutton = new ImageView(context);
		extraPUbutton.setImageResource(R.drawable.extra_powerup);
		RelativeLayout.LayoutParams extraPUbutton_params = new LayoutParams(buttonSide, buttonSide);
		extraPUbutton_params.setMargins((int)(OBJECT_WIDTH*0.573-buttonSide/2), PUbuttonsY, 0, 0);
		extraPUbutton.setLayoutParams(extraPUbutton_params);
		extraPUbutton.setOnTouchListener(this);
		addView(extraPUbutton);
	    
	    //rect
	    rect2 = new View(context);
	    RelativeLayout.LayoutParams rect2_params = new RelativeLayout.LayoutParams((int)(OBJECT_WIDTH*0.007),(int)(OBJECT_HEIGHT*0.8));
	    rect2_params.setMargins((int) (OBJECT_WIDTH*0.666), (int)(OBJECT_HEIGHT*0.1), 0, (int)(OBJECT_HEIGHT*0.1));
	    rect2.setLayoutParams(rect2_params);
	    rect2.setBackgroundColor(Color.WHITE);
	    addView(rect2);
	    
	    //immu PU Label
	  	immuPUtext = new TextView(context);
	  	immuPUtext.setText("0");
	  	RelativeLayout.LayoutParams immuPUtext_params = new RelativeLayout.LayoutParams(textWidth, LayoutParams.WRAP_CONTENT);
	  	immuPUtext_params.setMargins((int) (OBJECT_WIDTH*0.666), PUtextY, 0, 0);
	  	immuPUtext.setLayoutParams(immuPUtext_params);
	  	immuPUtext.setTextColor(Color.WHITE);
	  	immuPUtext.setTextSize(textSize);
	    immuPUtext.setGravity(Gravity.CENTER);
	    immuPUtext.setTypeface(Helveticaneue_light);
	  	addView(immuPUtext);
	    
	    //immu PU Button
	  	immuPUbutton = new ImageView(context);
	  	immuPUbutton.setImageResource(R.drawable.immu_powerup);
	  	RelativeLayout.LayoutParams immuPUbutton_params = new LayoutParams(buttonSide, buttonSide);
	  	immuPUbutton_params.setMargins((int) (OBJECT_WIDTH*0.906-buttonSide/2), PUbuttonsY, 0, 0);
	  	immuPUbutton.setLayoutParams(immuPUbutton_params);
	  	immuPUbutton.setOnTouchListener(this);
	  	addView(immuPUbutton);

	  	//immu progress bar(circular)
	  	immuProgress = new SRProgressWheel(context);
	  	immuProgress.setLayoutParams(immuPUbutton_params);
	  	immuProgress.setVisibility(View.GONE);
	  	addView(immuProgress);	  	
	}
	
	//http://stackoverflow.com/questions/18835166/check-if-motionevent-action-up-is-out-of-the-imageview
	Rect rect;	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
			if(v == extraPUbutton){
				extraPUbutton.setImageResource(R.drawable.extra_powerup_pressed);
			}else if(v == immuPUbutton){
				immuPUbutton.setImageResource(R.drawable.immu_powerup_pressed);
			}else if(v == comboPUbutton){
				comboPUbutton.setImageResource(R.drawable.combo_powerup_pressed);
			}
			break;
		case MotionEvent.ACTION_UP:
			Boolean isOn = true;
			if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                isOn = false;
            }
			
			if(v == extraPUbutton){
				if(isOn)
				mExtraListener.onEvent();
				extraPUbutton.setImageResource(R.drawable.extra_powerup);
			}else if(v == immuPUbutton){
				if(isOn)
				mImmuListener.onEvent();
				immuPUbutton.setImageResource(R.drawable.immu_powerup);
			}else if(v == comboPUbutton){
				if(isOn)
				mComboListener.onEvent();
				comboPUbutton.setImageResource(R.drawable.combo_powerup);
			}
			break;
		default:
			break;
		}
		return true;
	}
	
	public void setExtraText(String extra){
		extraPUtext.setText(extra);
	}
	
	public void setComboText(String combo){
		comboPUtext.setText(combo);
	}
	
	public void setImmuText(String immu){
		immuPUtext.setText(immu);
	}
	
	public void setComboProgress(int progress){
		comboProgress.setProgress(progress);
	}
	
	public void setImmuProgress(int progress){
		immuProgress.setProgress(progress);
	}
	
	public void showComboProgress(Boolean show){
		if(show){
			comboProgress.setVisibility(View.VISIBLE);
		}else{
			comboProgress.setVisibility(View.GONE);
		}
	}
	
	public void showImmuProgress(Boolean show){
		if(show){
			immuProgress.setVisibility(View.VISIBLE);
		}else{
			immuProgress.setVisibility(View.GONE);
		}
	}
}
