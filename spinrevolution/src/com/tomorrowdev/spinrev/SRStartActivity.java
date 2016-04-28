/**
 * Created on 01/10/2014 by Gabriel Esteban
 * Tomorrow Developers SCP, 2014
 */
package com.tomorrowdev.spinrev;

import com.tomorrowdev.spinrev.views.SRTutorial;
import com.tomorrowdev.spinrev.views.SRTutorial.FinishListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * Launcher Activity and intermediate activity between {@link: SRScoreActivity} and
 * the {@link: SRTutorial}.
 * 
 * @author Gabriel Esteban
 */
public class SRStartActivity extends Activity {

	private boolean shouldTutorialBeShowed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent().getBooleanExtra("showTutorial", false)){
			shouldTutorialBeShowed = true;
		}
		
		//init data mechanisms, this init includes the SRUserData init
		SRGameManager.getInstace().init(this);
		//resetting all the variables, to solve the bug that creates closing and opening the app
		SRGameManager.getInstace().resetGame();
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
	    //Show the tutorial
	    if(SRUserData.getInstace().isFirstLoad() || shouldTutorialBeShowed){
	    	SRTutorial tutorial = new SRTutorial(this);
	    	tutorial.setFinishListener(new FinishListener() {				
				@Override
				public void onFinish() {
					Intent intent = new Intent(SRStartActivity.this, SRGameActivity.class);
					startActivity(intent);
					finish();
				}
			});
	    	RelativeLayout.LayoutParams tutorial_params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    	tutorial.setLayoutParams(tutorial_params);
	    	tutorial.setSize(displaymetrics.widthPixels, displaymetrics.heightPixels, displaymetrics.density);
	    	setContentView(tutorial);
	    } else {
	    	Intent intent = new Intent(SRStartActivity.this, SRGameActivity.class);
			startActivity(intent);
			finish();
	    }
	}
}
