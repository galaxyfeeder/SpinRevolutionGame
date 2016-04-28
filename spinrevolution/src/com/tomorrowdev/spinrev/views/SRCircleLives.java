package com.tomorrowdev.spinrev.views;

import java.util.Random;

import com.tomorrowdev.spinrev.R;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class SRCircleLives extends RelativeLayout{

	SRColorCircleView life0, life1, life2, life3, life4;
	
	int CurrentLife = 1;
	int addingLifeIndex = 0;
	int totalLifes = 5;
	int[] ActualColors = new int[5];
	SRColorCircleView[] lives = new SRColorCircleView[5];
	
	int OBJECT_WIDTH, OBJECT_HEIGHT;
	Context context;
	AudioManager audio;
	
	public SRCircleLives(Context context) {
		super(context);
		
		this.context = context;
	}
	
	public void setSize(int width, int height){
		OBJECT_WIDTH = width;
		OBJECT_HEIGHT = height;
		
		sharedConstructor(context);
	}
	
	private void sharedConstructor(Context context) {
		
		setBackgroundColor(Color.TRANSPARENT);
		
		shuffleArray();
		
		int circleSide = (int)(OBJECT_HEIGHT/2);
		int circleY = (int)(OBJECT_HEIGHT*0.5)-circleSide/2;
		
		life0 = new SRColorCircleView(context);
		RelativeLayout.LayoutParams life0_params = new RelativeLayout.LayoutParams(circleSide, circleSide);
		life0_params.setMargins((int)(OBJECT_WIDTH*0.1)-circleSide/2, circleY, 0, 0);
		life0.setLayoutParams(life0_params);
		life0.changeColor(ActualColors[0]);
		addView(life0);
		
		life1 = new SRColorCircleView(context);
		RelativeLayout.LayoutParams life1_params = new RelativeLayout.LayoutParams(circleSide, circleSide);
		life1_params.setMargins((int)(OBJECT_WIDTH*0.3)-circleSide/2, circleY, 0, 0);
		life1.setLayoutParams(life1_params);
		life1.changeColor(ActualColors[1]);
		addView(life1);
		
		life2 = new SRColorCircleView(context);
		RelativeLayout.LayoutParams life2_params = new RelativeLayout.LayoutParams(circleSide, circleSide);
		life2_params.setMargins((int)(OBJECT_WIDTH*0.5)-circleSide/2, circleY, 0, 0);
		life2.setLayoutParams(life2_params);
		life2.changeColor(ActualColors[2]);
		addView(life2);
		
		life3 = new SRColorCircleView(context);
		RelativeLayout.LayoutParams life3_params = new RelativeLayout.LayoutParams(circleSide, circleSide);
		life3_params.setMargins((int)(OBJECT_WIDTH*0.7)-circleSide/2, circleY, 0, 0);
		life3.setLayoutParams(life3_params);
		life3.changeColor(ActualColors[3]);
		addView(life3);
		
		life4 = new SRColorCircleView(context);
		RelativeLayout.LayoutParams life4_params = new RelativeLayout.LayoutParams(circleSide, circleSide);
		life4_params.setMargins((int)(OBJECT_WIDTH*0.9)-circleSide/2, circleY, 0, 0);
		life4.setLayoutParams(life4_params);
		life4.changeColor(ActualColors[4]);
		addView(life4);
		
		lives[0] = life0;
		lives[1] = life1;
		lives[2] = life2;
		lives[3] = life3;
		lives[4] = life4;
		
		audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		firstLiveAnimation();
	}
	
	public void shuffleArray(){
		int[] ColorsArray = {Color.rgb(255, 0, 0),	//RED
				 Color.rgb(30, 224, 30),			//GREEN
				 Color.rgb(0, 0, 255),				//BLUE
				 Color.rgb(255, 112, 0),			//ORANGE
				 Color.rgb(255, 0, 255),			//PURPLE
				 Color.rgb(255, 255, 0)};			//YELLOW
		
		//http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
		
		Random rand = new Random();
	    for (int i = ColorsArray.length - 1; i > 0; i--){
	    	int index = rand.nextInt(i + 1);
	    	int a = ColorsArray[index];
	    	ColorsArray[index] = ColorsArray[i];
	    	ColorsArray[i] = a;
	    }
	    ActualColors[0] = ColorsArray[0];
	    ActualColors[1] = ColorsArray[1];
	    ActualColors[2] = ColorsArray[2];
	    ActualColors[3] = ColorsArray[3];
	    ActualColors[4] = ColorsArray[4];
	}

	public int nextColor() {
		//Call this function before than nextLife
		return ActualColors[CurrentLife-1];
	}
	
	public void nextLife(){
		final SRColorCircleView lostLife = lives[CurrentLife-1];
		if(CurrentLife != 5){
			final SRColorCircleView newLife = lives[CurrentLife];
			CurrentLife++;
			
			Animation deadanim = AnimationUtils.loadAnimation(context, R.anim.circle_dead);
			Animation startanim = AnimationUtils.loadAnimation(context, R.anim.circle_start);
						
			newLife.startAnimation(startanim);			
			lostLife.startAnimation(deadanim);
			
			if(audio.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
				Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
				// Vibrate for 500 milliseconds
				v.vibrate(500);
			}		
			
			totalLifes--;
			
		}else{
			CurrentLife = 1;
			final SRColorCircleView newLife = lives[0];
			
			Animation deadanim = AnimationUtils.loadAnimation(context, R.anim.circle_dead);
			Animation startanim = AnimationUtils.loadAnimation(context, R.anim.circle_start);
						
			newLife.startAnimation(startanim);			
			lostLife.startAnimation(deadanim);
			
			if(audio.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
				Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
				// Vibrate for 500 milliseconds
				v.vibrate(500);
			}
			
			totalLifes--;
		}
	}
	
	public void firstLiveAnimation(){
		Animation startanim = AnimationUtils.loadAnimation(context, R.anim.circle_start);
		life0.startAnimation(startanim);
	}
	
	public void addLife(){
		totalLifes++;
		Animation restartanim = AnimationUtils.loadAnimation(context, R.anim.circle_restart);
		switch (addingLifeIndex) {
		case 0:life0.startAnimation(restartanim);break;
		case 1:life1.startAnimation(restartanim);break;
		case 2:life2.startAnimation(restartanim);break;
		case 3:life3.startAnimation(restartanim);break;
		case 4:life4.startAnimation(restartanim);break;
		default:break;
		}
		addingLifeIndex++;
		if(addingLifeIndex == 4)
			addingLifeIndex = 0;
	}
}
