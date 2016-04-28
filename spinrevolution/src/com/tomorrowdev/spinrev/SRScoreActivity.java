package com.tomorrowdev.spinrev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.tomorrowdev.spinrev.util.IabHelper;
import com.tomorrowdev.spinrev.util.IabResult;
import com.tomorrowdev.spinrev.util.Inventory;
import com.tomorrowdev.spinrev.util.Purchase;
import com.tomorrowdev.spinrev.views.SRPUHeader;
import com.tomorrowdev.spinrev.views.SRPointsMenu;
import com.tomorrowdev.spinrev.views.SRPopUpMenu;
import com.tomorrowdev.spinrev.views.SRPowerUPMenu;
import com.tomorrowdev.spinrev.views.SRPUHeader.OnComboPUEventListener;
import com.tomorrowdev.spinrev.views.SRPUHeader.OnExtraPUEventListener;
import com.tomorrowdev.spinrev.views.SRPUHeader.OnImmuPUEventListener;
import com.tomorrowdev.spinrev.views.SRPointsMenu.OnIAEventListener;
import com.tomorrowdev.spinrev.views.SRPowerUPMenu.OnBuyEventListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SRScoreActivity extends Activity implements OnTouchListener{

	//http://www.techotopia.com/index.php/Integrating_Google_Play_In-app_Billing_into_an_Android_Application_%E2%80%93_A_Tutorial
	
	RelativeLayout container;
	SRPUHeader header;
	TextView scoreTitleText, finalScoreText, totalPoints, walletText, bestScore, needHelpText = null, tapToBuyText;
	ImageView shareButton, replayButton, buyPoints, leaderboardsButton;
	View color;
	LinearLayout scoreContainer, pointContainer;
	SRPowerUPMenu powerMenu;
	SRPointsMenu pointsMenu;
	Animation menu_in, menu_out, fons_in, fons_out;
	Resources res;
	
	IabHelper mIAHelper;
	GameHelper mGameHelper;
	
	float DENSITY;
	int SCREEN_WIDTH, SCREEN_HEIGHT;
	int powerUpPrice = 1000;
	int buyQuantity;
	
	//mixpanel
	MixpanelAPI mixpanel;
	public static final String MIXPANEL_TOKEN = "e8429217f5a5aa0dec4a893d94960c2d";
	Boolean shouldDataBeSended = true;
	
	//play game services
	AccomplishmentsOutbox mOutbox;
	
	//Ads
	private AdView adView;
	private static final String AD_UNIT_ID = "ca-app-pub-3390829016396377/3506374840";
	
	//request codes we use when invoking an external activity
    final int RC_RESOLVE = 5000, RC_UNUSED = 5001;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		new serverVariables().execute();
		
		SCREEN_WIDTH = displaymetrics.widthPixels;
		SCREEN_HEIGHT = displaymetrics.heightPixels;
		DENSITY = displaymetrics.density;
		
		res = getResources();
		
		container = new RelativeLayout(this);
		FrameLayout.LayoutParams container_params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		container.setLayoutParams(container_params);		
		
		//background
		container.setBackgroundColor(Color.rgb(255, 255, 255));
		
		//header
	    int headerHeight = (int)(SCREEN_WIDTH*0.18);
	    header = new SRPUHeader(this);
	    header.setSize(SCREEN_WIDTH, headerHeight, DENSITY);
	    RelativeLayout.LayoutParams header_params = new RelativeLayout.LayoutParams(SCREEN_WIDTH, headerHeight);
	    header.setLayoutParams(header_params);
	    header.setExtraPUEventListener(new OnExtraPUEventListener() {			
			@Override
			public void onEvent() {
				color.setBackgroundColor(Color.RED);
				pointsMenu.closePointsMenu();
				powerMenu.openPowerUpMenu(SRPowerUPMenu.EXTRA_POWERUP);
				color.setVisibility(View.VISIBLE);				
				powerMenu.startAnimation(menu_in);
				color.startAnimation(fons_in);
			}
		});
	    header.setComboPUEventListener(new OnComboPUEventListener() {			
			@Override
			public void onEvent() {
				color.setBackgroundColor(Color.rgb(254, 154, 46));
				powerMenu.openPowerUpMenu(SRPowerUPMenu.COMBO_POWERUP);
				color.setVisibility(View.VISIBLE);
				pointsMenu.closePointsMenu();
				powerMenu.startAnimation(menu_in);
				color.startAnimation(fons_in);
			}
		});
	    header.setImmuPUEventListener(new OnImmuPUEventListener() {			
			@Override
			public void onEvent() {
				color.setBackgroundColor(Color.BLUE);
				powerMenu.openPowerUpMenu(SRPowerUPMenu.IMMU_POWERUP);
				color.setVisibility(View.VISIBLE);
				pointsMenu.closePointsMenu();
				powerMenu.startAnimation(menu_in);
				color.startAnimation(fons_in);
			}
		});
	    header.setExtraText(""+SRGameManager.getInstace().getExtraRemaining());
	    header.setComboText(""+SRGameManager.getInstace().getComboRemaining());
	    header.setImmuText(""+SRGameManager.getInstace().getImmuRemaining());
	    container.addView(header);    
		
	    Typeface Helveticaneue_medium = Typeface.createFromAsset(getAssets(), "fonts/Helveticaneue-medium.ttf");
	    Typeface Helveticaneue_light = Typeface.createFromAsset(getAssets(), "fonts/Helveticaneue-light.ttf");
	    Typeface Helveticaneue_bold = Typeface.createFromAsset(getAssets(), "fonts/Helveticaneue-bold.ttf");
	    
	    //Score container
	    scoreContainer = new LinearLayout(this);
	  	RelativeLayout.LayoutParams scoreContainer_params = new RelativeLayout.LayoutParams(SCREEN_WIDTH, (int)(SCREEN_HEIGHT*0.22));
	  	scoreContainer_params.setMargins(0, (int)(0.23*SCREEN_HEIGHT), 0, 0);
	  	scoreContainer.setLayoutParams(scoreContainer_params);
	  	scoreContainer.setOrientation(LinearLayout.VERTICAL);
	  	scoreContainer.setBackgroundColor(Color.RED);
	  	scoreContainer.setWeightSum(100);
	  	container.addView(scoreContainer);
	  	
	  	//score title
	  	scoreTitleText = new TextView(this);
	  	scoreTitleText.setText(res.getString(R.string.your_score));
	    LinearLayout.LayoutParams scoreTitleText_params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0);
	    scoreTitleText_params.weight = 50;
	    scoreTitleText_params.setMargins((int)(SCREEN_WIDTH*0.06), 0, 0, 0);
	    scoreTitleText.setLayoutParams(scoreTitleText_params);
	    scoreTitleText.setTextColor(Color.WHITE);
	    scoreTitleText.setGravity(Gravity.CENTER);
	    scoreTitleText.setTextSize((int)(SCREEN_WIDTH/DENSITY*0.12));
	    scoreTitleText.setTypeface(Helveticaneue_medium);
	    scoreContainer.addView(scoreTitleText);
	  	
	    //second line container
	    RelativeLayout secondLineContainer = new RelativeLayout(this);
	    LinearLayout.LayoutParams secondLineContainer_params = new LinearLayout.LayoutParams(SCREEN_WIDTH, 0);
	    secondLineContainer_params.weight = 50;
	  	secondLineContainer.setLayoutParams(secondLineContainer_params);
	  	secondLineContainer.setGravity(Gravity.CENTER);
	  	scoreContainer.addView(secondLineContainer);
	    
	    //score
	    finalScoreText = new TextView(this);
	    finalScoreText.setText(""+SRGameManager.getInstace().getScore());
	    RelativeLayout.LayoutParams finalScoreText_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    finalScoreText_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	    finalScoreText_params.setMargins((int)(SCREEN_WIDTH*0.06), (int)(-SCREEN_HEIGHT*0.02), 0, 0);
	    finalScoreText.setLayoutParams(finalScoreText_params);
	    finalScoreText.setTextColor(Color.YELLOW);
	    finalScoreText.setTextSize((int) (SCREEN_WIDTH/DENSITY*0.12));
	    finalScoreText.setTypeface(Helveticaneue_bold);
	    finalScoreText.setGravity(Gravity.LEFT);
	    secondLineContainer.addView(finalScoreText);
	    
	    //best score
	    bestScore = new TextView(this);
	    bestScore.setText(res.getString(R.string.best)+" "+SRGameManager.getInstace().getBestScore());
	    RelativeLayout.LayoutParams bestScore_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    bestScore_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	    bestScore_params.setMargins(0, 0, (int)(SCREEN_WIDTH*0.06), 0);
	    bestScore.setLayoutParams(bestScore_params);
	    bestScore.setTextColor(Color.WHITE);
	    bestScore.setTextSize((int) (SCREEN_WIDTH/DENSITY*0.08));
	    bestScore.setGravity(Gravity.RIGHT);
	    bestScore.setTypeface(Helveticaneue_light);
	    secondLineContainer.addView(bestScore);
	    
	    int buttonSide = (int) (SCREEN_WIDTH * 0.2);
	    int replayButtonSide = (int)(SCREEN_WIDTH*0.3);
	    int buttonsY = (int) (SCREEN_HEIGHT*0.64);
	    
	    //share button
	    shareButton = new ImageView(this);
	    shareButton.setImageResource(R.drawable.sharebutton);
	  	RelativeLayout.LayoutParams shareButton_params = new RelativeLayout.LayoutParams(buttonSide, buttonSide);
	  	shareButton_params.setMargins((int) (SCREEN_WIDTH*0.2)-buttonSide/2, buttonsY-buttonSide/2, 0, 0);
	  	shareButton.setLayoutParams(shareButton_params);
	  	shareButton.setOnTouchListener(this);
	  	container.addView(shareButton);
	  	
	  	//replay button
	  	replayButton = new ImageView(this);
	  	replayButton.setImageResource(R.drawable.replaybutton);
	  	RelativeLayout.LayoutParams replayButton_params = new RelativeLayout.LayoutParams(replayButtonSide, replayButtonSide);
	  	replayButton_params.setMargins((int) (SCREEN_WIDTH*0.5)-replayButtonSide/2, buttonsY-replayButtonSide/2, 0, 0);
	  	replayButton.setLayoutParams(replayButton_params);
	  	replayButton.setOnTouchListener(this);
	  	container.addView(replayButton);
	    
	  	//leaderboards button
	  	leaderboardsButton = new ImageView(this);
	  	leaderboardsButton.setImageResource(R.drawable.gamebutton);
	  	RelativeLayout.LayoutParams leaderboardsButton_params = new RelativeLayout.LayoutParams(buttonSide, buttonSide);
	  	leaderboardsButton_params.setMargins((int) (SCREEN_WIDTH*0.8)-buttonSide/2, buttonsY-buttonSide/2, 0, 0);
	  	leaderboardsButton.setLayoutParams(leaderboardsButton_params);
	  	leaderboardsButton.setOnTouchListener(this);
	  	container.addView(leaderboardsButton);
	  	
	  	//wallet container
	  	pointContainer = new LinearLayout(this);
	  	RelativeLayout.LayoutParams pointContainer_params = new RelativeLayout.LayoutParams(SCREEN_WIDTH, LayoutParams.WRAP_CONTENT);
	  	pointContainer_params.setMargins(0, (int)(0.8*SCREEN_HEIGHT), 0, 0);
	  	pointContainer.setGravity(Gravity.CENTER);
	  	pointContainer.setLayoutParams(pointContainer_params);
	  	pointContainer.setOrientation(LinearLayout.HORIZONTAL);
	  	container.addView(pointContainer);
	  	
	  	//wallet label
	  	walletText = new TextView(this);
	  	walletText.setText(res.getString(R.string.wallet)+" ");
	    RelativeLayout.LayoutParams walletText_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    walletText.setLayoutParams(walletText_params);
	    walletText.setTextColor(Color.GRAY);
	    walletText.setTextSize((int) (SCREEN_WIDTH/DENSITY * 0.1));
	    walletText.setGravity(Gravity.CENTER);
	    walletText.setTypeface(Helveticaneue_light);
	    pointContainer.addView(walletText);
	  	
	  	//Total Points
	  	totalPoints = new TextView(this);
	  	totalPoints.setText(SRGameManager.getInstace().getTotalPoints()+"  ");
	    RelativeLayout.LayoutParams totalPoints_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    totalPoints.setLayoutParams(totalPoints_params);
	    totalPoints.setTextColor(Color.RED);
	    totalPoints.setTextSize((int) (SCREEN_WIDTH/DENSITY * 0.1));
	    totalPoints.setGravity(Gravity.CENTER);
	    totalPoints.setTypeface(Helveticaneue_bold);
	    pointContainer.addView(totalPoints);
	  	
	    int pointsButtonSide = (int)(SCREEN_WIDTH*0.12);
	    
	    //Buy points button
	    buyPoints = new ImageView(this);
	    buyPoints.setImageResource(R.drawable.buypoints);
	  	RelativeLayout.LayoutParams buyPoints_params = new RelativeLayout.LayoutParams(pointsButtonSide, pointsButtonSide);
	  	buyPoints.setLayoutParams(buyPoints_params);
	  	buyPoints.setOnTouchListener(this);
	  	pointContainer.addView(buyPoints);
	    
	  	//Ads
	  	adView = new AdView(this);
	  	adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId(AD_UNIT_ID);
	    RelativeLayout.LayoutParams adView_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    adView_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	    adView_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    adView.setLayoutParams(adView_params);
	    container.addView(adView);
	    AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        .addTestDevice("252B9BC3B255D185B51C2541B929CA39")
        .build();
	    adView.loadAd(adRequest);
	  	
	    //Tap to buy button
	    tapToBuyText = new TextView(this);
	    tapToBuyText.setText(res.getString(R.string.tapToBuy));
	    RelativeLayout.LayoutParams tapToBuyText_params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)(SCREEN_WIDTH*0.09));
	    tapToBuyText_params.setMargins(0, (int)(SCREEN_WIDTH*0.16), 0, 0);
	    tapToBuyText.setLayoutParams(tapToBuyText_params);
	    tapToBuyText.setTextColor(Color.WHITE);
	    tapToBuyText.setGravity(Gravity.CENTER);
	    tapToBuyText.setBackgroundColor(Color.rgb(30, 225, 30));
	    tapToBuyText.setTextSize((int)(SCREEN_WIDTH/DENSITY*0.07));
	    tapToBuyText.setTypeface(Helveticaneue_light);
	    container.addView(tapToBuyText);
	    
	    //Need help button when points scored are less than 200
	    if(SRGameManager.getInstace().getScore() < 200){
	    	needHelpText = new TextView(this);
	    	needHelpText.setText(res.getString(R.string.needHelp));
		    RelativeLayout.LayoutParams needHelpText_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, (int)(SCREEN_WIDTH*0.1));
		    needHelpText_params.setMargins(0, (int)(SCREEN_HEIGHT*0.45), (int)(SCREEN_WIDTH*0.03), 0);
		    needHelpText_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		    needHelpText.setLayoutParams(needHelpText_params);
		    needHelpText.setTextColor(Color.RED);
		    needHelpText.setGravity(Gravity.CENTER);
		    needHelpText.setTextSize((int)(SCREEN_WIDTH/DENSITY*0.07));
		    needHelpText.setTypeface(Helveticaneue_light);
		    needHelpText.setOnTouchListener(this);
		    container.addView(needHelpText);
	    }
	    
	  	//View for changing color
	  	color = new View(this);
	  	color.setBackgroundColor(Color.TRANSPARENT);
	  	RelativeLayout.LayoutParams color_params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, SCREEN_HEIGHT-headerHeight);
	  	color_params.setMargins(0, headerHeight, 0, 0);
	  	color.setLayoutParams(color_params);
	  	color.setOnTouchListener(this);
	  	color.setVisibility(View.GONE);
	  	container.addView(color);
	  	
	  	int powerMenuSide = SCREEN_WIDTH/10*9;
	  	
	  	//powerMenu
	  	powerMenu = new SRPowerUPMenu(this);
	  	powerMenu.setSize(powerMenuSide, powerMenuSide, DENSITY);
	  	RelativeLayout.LayoutParams powerMenu_params = new RelativeLayout.LayoutParams(powerMenuSide, powerMenuSide);
	  	powerMenu_params.setMargins(SCREEN_WIDTH/20, headerHeight+(SCREEN_HEIGHT-headerHeight)/2-powerMenuSide/2, 0, 0);
	  	powerMenu.setLayoutParams(powerMenu_params);
	  	powerMenu.setBuyEventListener(new OnBuyEventListener() {			
			@Override
			public void onEvent(int type, int quantity) {
				closeMenu(0);
				SRGameManager.getInstace().setTotalPoints(SRGameManager.getInstace().getTotalPoints() - quantity*powerUpPrice);
				totalPoints.setText(SRGameManager.getInstace().getTotalPoints()+" ");
				
				//Send event
				JSONObject props = new JSONObject();
	    		try {
					props.put("type", type);
					props.put("quantity", quantity);
				} catch (JSONException e) {
					e.printStackTrace();
				}
	    		mixpanel.track("PUbought", props);
				
				switch (type) {
				case SRPowerUPMenu.EXTRA_POWERUP:
					SRGameManager.getInstace().setExtraRemaining(SRGameManager.getInstace().getExtraRemaining()+quantity);
					header.setExtraText(""+SRGameManager.getInstace().getExtraRemaining());
					break;
				case SRPowerUPMenu.IMMU_POWERUP:
					SRGameManager.getInstace().setImmuRemaining(SRGameManager.getInstace().getImmuRemaining()+quantity);
					header.setImmuText(""+SRGameManager.getInstace().getImmuRemaining());
					break;
				case SRPowerUPMenu.COMBO_POWERUP:
					SRGameManager.getInstace().setComboRemaining(SRGameManager.getInstace().getComboRemaining()+quantity);
					header.setComboText(""+SRGameManager.getInstace().getComboRemaining());
					break;
				default:
					break;
				}
			}
		});
	  	container.addView(powerMenu);
	  	
	  	//pointsMenu
	  	pointsMenu = new SRPointsMenu(this);
	  	pointsMenu.setSize(powerMenuSide, powerMenuSide, DENSITY);
	  	RelativeLayout.LayoutParams iaMenu_params = new RelativeLayout.LayoutParams(powerMenuSide, powerMenuSide);
	  	iaMenu_params.setMargins(SCREEN_WIDTH/20, headerHeight+(SCREEN_HEIGHT-headerHeight)/2-powerMenuSide/2, 0, 0);
	  	pointsMenu.setLayoutParams(iaMenu_params);
	  	pointsMenu.setIAEventListener(new OnIAEventListener() {			
			@Override
			public void onEvent(int type) {
				closeMenu(1);				
				gainPoints(type);
			}
		});
	  	container.addView(pointsMenu);
	  	
	  	//load animations
	  	menu_in = AnimationUtils.loadAnimation(this, R.anim.menu_in);
	  	menu_out = AnimationUtils.loadAnimation(this, R.anim.menu_out);
	  	fons_in = AnimationUtils.loadAnimation(this, R.anim.fons_in);
	  	fons_out = AnimationUtils.loadAnimation(this, R.anim.fons_out);
	    
	    //menu_in animation listener
	    menu_in.setAnimationListener(new AnimationListener() {					
			@Override
			public void onAnimationStart(Animation animation) {}					
			@Override
			public void onAnimationRepeat(Animation animation) {}					
			@Override
			public void onAnimationEnd(Animation animation) {
				scoreContainer.setVisibility(View.GONE);
				shareButton.setVisibility(View.GONE);
				replayButton.setVisibility(View.GONE);
				leaderboardsButton.setVisibility(View.GONE);
				pointContainer.setVisibility(View.GONE);
				if(needHelpText != null)
					needHelpText.setVisibility(View.GONE);
				tapToBuyText.setVisibility(View.GONE);
			}
		});
	    
	  	//change view if it's best score
	  	if(SRGameManager.getInstace().getScore() == SRGameManager.getInstace().getBestScore()){
	  		bestScore.setText("");
	  		scoreTitleText.setText(res.getString(R.string.best_score));
	  	}
	  	
		//set container as the main view
		setContentView(container);
		
		//in-app billing
		String base64EncodedPublicKey = 
		              "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnrmZNlk/wHCrAdzfuSautxECXEp/DRK2Z5rFwlmvKRV7WDJxwn+cUKoBcZmLve1SgVUVUlPPI2+YcJaLyPR00is7HdBFfIUvLoYHEYTBLRoc2/HUu094LuE3FStD7CRHKHRCjDZViX6iUnphhq/wyJFww+b8xkeLQ1Q2iCiU3+dI8ABFjcdqSLpxCLZEo+rqE1IOwx8vb6vShRVDRZ3kug9hrwy2CHSgsimhAyqC781D89LUtppqchtbVgcihbbHfIyZO9PSbp2fmqFdWks2uBEPgkYQEW4ZkWsIGMHFpZXd7gVDwMIZyovGufCGIZAq3cAytlW9oc0A+DAj6X1lswIDAQAB";
				
		mIAHelper = new IabHelper(this, base64EncodedPublicKey);
		        
		mIAHelper.startSetup(new 
		IabHelper.OnIabSetupFinishedListener() {
		   	 public void onIabSetupFinished(IabResult result){
		           if (!result.isSuccess()) {
		        	   Log.d("TAG", "In-app Billing setup failed: " + result);
		           } else {             
		        	   Log.d("TAG", "In-app Billing is set up OK");
		           }
		   	 }
		});
		
		//instantiate the class that save the achievements
		mOutbox = new AccomplishmentsOutbox();
		
		// create game helper with all APIs (Games, Plus, AppState):
	    mGameHelper = new GameHelper(this, GameHelper.CLIENT_ALL);
	    // enable debug logs (if applicable)
	    mGameHelper.enableDebugLog(true);
	    GameHelperListener listener = new GameHelper.GameHelperListener() {
	        @Override
	        public void onSignInSucceeded() {
	            // push those accomplishments to the cloud, if signed in
	            pushAcomplishments();
	            
	            //send user name to mixpanel when the users signs in
	            JSONObject props = new JSONObject();
		    	try {
					props.put("$name", Games.Players.getCurrentPlayer(mGameHelper.getApiClient()).getDisplayName());
				} catch (JSONException e) {
					e.printStackTrace();
				}
		    	mixpanel.getPeople().set(props);
	        }
	        @Override
	        public void onSignInFailed() {
	        }
	    };
	    mGameHelper.setup(listener);	
				
		//play game services
        mOutbox.loadLocal(this);        
        // check for achievements
        checkForAchievements(SRGameManager.getInstace().getScore());
        // update leaderboards
        updateLeaderboards(SRGameManager.getInstace().getScore());
        
        //mixpanel
        mixpanel = MixpanelAPI.getInstance(this, MIXPANEL_TOKEN);
        String device_uuid = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        mixpanel.identify(device_uuid);
	    mixpanel.getPeople().identify(device_uuid);
	    
	    //Save another gamePlayed
	    SRGameManager.getInstace().incrementGamesPlayed(1);
	}
	
	/** 
	 * Listener that is called when the in-app billing is finished.
	 * */
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			if (result.isFailure()) {
				return;
	    	} else if(purchase.getSku().equals(SRPointsMenu.POINTS_20_BUTTON_SKU)) {
	    		JSONObject props = new JSONObject();
	    		try {
					props.put("type", "1");
				} catch (JSONException e) {
					e.printStackTrace();
				}
	    		mixpanel.track("IAbought", props);
	    		mIAHelper.queryInventoryAsync(mGotInventoryListener);
	    		
				
			} else if(purchase.getSku().equals(SRPointsMenu.POINTS_50_BUTTON_SKU)) {
				JSONObject props = new JSONObject();
	    		try {
					props.put("type", "2");
				} catch (JSONException e) {
					e.printStackTrace();
				}
	    		mixpanel.track("IAbought", props);
	    		mIAHelper.queryInventoryAsync(mGotInventoryListener);
				
			} else if(purchase.getSku().equals(SRPointsMenu.POINTS_100_BUTTON_SKU)) {
				JSONObject props = new JSONObject();
	    		try {
					props.put("type", "3");
				} catch (JSONException e) {
					e.printStackTrace();
				}
	    		mixpanel.track("IAbought", props);
	    		mIAHelper.queryInventoryAsync(mGotInventoryListener);
			}
		}
	};
	
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener 
	   = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			if (result.isFailure()) {
				// handle error here
			}
			else {
				if(inventory.hasPurchase(SRPointsMenu.POINTS_20_BUTTON_SKU)){
					mIAHelper.consumeAsync(inventory.getPurchase(SRPointsMenu.POINTS_20_BUTTON_SKU), 
							   mConsumeFinishedListener);
				} else if(inventory.hasPurchase(SRPointsMenu.POINTS_50_BUTTON_SKU)){
					mIAHelper.consumeAsync(inventory.getPurchase(SRPointsMenu.POINTS_50_BUTTON_SKU), 
							   mConsumeFinishedListener);
				} else if(inventory.hasPurchase(SRPointsMenu.POINTS_100_BUTTON_SKU)){
					mIAHelper.consumeAsync(inventory.getPurchase(SRPointsMenu.POINTS_100_BUTTON_SKU), 
							   mConsumeFinishedListener);
				}
			}
		}
	};
	
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
	new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			if (result.isSuccess()) {
				if(purchase.getSku().equals(SRPointsMenu.POINTS_20_BUTTON_SKU)){
		    		
		    		SRGameManager.getInstace().setTotalPoints(SRGameManager.getInstace().getTotalPoints() + 20000);
					totalPoints.setText(SRGameManager.getInstace().getTotalPoints()+" ");
					
				} else if(purchase.getSku().equals(SRPointsMenu.POINTS_50_BUTTON_SKU)){
		    		
		    		SRGameManager.getInstace().setTotalPoints(SRGameManager.getInstace().getTotalPoints() + 50000);
					totalPoints.setText(SRGameManager.getInstace().getTotalPoints()+" ");
					
				} else if(purchase.getSku().equals(SRPointsMenu.POINTS_100_BUTTON_SKU)){
		    		
		    		SRGameManager.getInstace().setTotalPoints(SRGameManager.getInstace().getTotalPoints() + 100000);
					totalPoints.setText(SRGameManager.getInstace().getTotalPoints()+" ");
					
				}
			}
			else {
				// handle error
			}
		}
	};
	
	public void gainPoints(int type){
		switch (type) {
		case SRPointsMenu.POINTS_20_BUTTON:
			mIAHelper.launchPurchaseFlow(this, SRPointsMenu.POINTS_20_BUTTON_SKU, 10001,   
	     			   mPurchaseFinishedListener, "mypurchasetoken");
			break;
		case SRPointsMenu.POINTS_15_BUTTON:
			if(Build.VERSION.SDK_INT >= 14){
				if(mixpanel.getPeople().getSurveyIfAvailable() != null){
					mixpanel.getPeople().showSurveyIfAvailable(this);
					SRUserData.getInstace().setSurveyDone(true);
					SRGameManager.getInstace().setTotalPoints(SRGameManager.getInstace().getTotalPoints() + 15000);
					totalPoints.setText(SRGameManager.getInstace().getTotalPoints()+" ");
				} else {
					AlertDialog.Builder dialog = new AlertDialog.Builder(SRScoreActivity.this);  
			        dialog.setTitle(res.getString(R.string.dialog_survey_title));  
			        dialog.setMessage(res.getString(R.string.dialog_survey_message));            
			        dialog.setCancelable(false);  
			        dialog.setPositiveButton(android.R.string.ok, null);
			        dialog.show();
				}
			}
			break;
		case SRPointsMenu.POINTS_50_BUTTON:
			mIAHelper.launchPurchaseFlow(this, SRPointsMenu.POINTS_50_BUTTON_SKU, 10003,   
	     			   mPurchaseFinishedListener, "mypurchasetoken");
			break;
		case SRPointsMenu.POINTS_100_BUTTON:
			mIAHelper.launchPurchaseFlow(this, SRPointsMenu.POINTS_100_BUTTON_SKU, 10004,   
	     			   mPurchaseFinishedListener, "mypurchasetoken");
			break;
		default:
			break;
		}
	}

	private void closeMenu(int type){
		//0 = PowerUp
		//1 = Points
		switch (type) {
		case 0:
			menu_out.setAnimationListener(new AnimationListener() {				
				@Override
				public void onAnimationStart(Animation animation) {
					scoreContainer.setVisibility(View.VISIBLE);
					shareButton.setVisibility(View.VISIBLE);
					replayButton.setVisibility(View.VISIBLE);
					leaderboardsButton.setVisibility(View.VISIBLE);
					pointContainer.setVisibility(View.VISIBLE);
					if(needHelpText != null)
						needHelpText.setVisibility(View.VISIBLE);
					tapToBuyText.setVisibility(View.VISIBLE);
					
				}				
				@Override
				public void onAnimationRepeat(Animation animation) {}				
				@Override
				public void onAnimationEnd(Animation animation) {
					color.setVisibility(View.GONE);
					powerMenu.closePoweUpMenu();
				}
			});
			powerMenu.startAnimation(menu_out);
			color.startAnimation(fons_out);
			break;
		case 1:
			menu_out.setAnimationListener(new AnimationListener() {				
				@Override
				public void onAnimationStart(Animation animation) {
					scoreContainer.setVisibility(View.VISIBLE);
					shareButton.setVisibility(View.VISIBLE);
					replayButton.setVisibility(View.VISIBLE);
					leaderboardsButton.setVisibility(View.VISIBLE);
					pointContainer.setVisibility(View.VISIBLE);
					if(needHelpText != null)
						needHelpText.setVisibility(View.VISIBLE);
					tapToBuyText.setVisibility(View.VISIBLE);
				}				
				@Override
				public void onAnimationRepeat(Animation animation) {}				
				@Override
				public void onAnimationEnd(Animation animation) {
					color.setVisibility(View.GONE);
					pointsMenu.closePointsMenu();
				}
			});
			pointsMenu.startAnimation(menu_out);
			color.startAnimation(fons_out);
			break;
		default:
			break;
		}
	}
	
	public void share() {
		//http://stackoverflow.com/questions/9730243/android-how-to-filter-specific-apps-for-action-send-intent
		
		String message = res.getString(R.string.share_message_A)+" "+SRGameManager.getInstace().getScore()+" "+res.getString(R.string.share_message_B);
	    String email_subject = "#SpinRevolution";
		
	    Intent emailIntent = new Intent();
	    emailIntent.setAction(Intent.ACTION_SEND);
	    emailIntent.putExtra(Intent.EXTRA_TEXT, message);
	    emailIntent.putExtra(Intent.EXTRA_SUBJECT, email_subject);
	    emailIntent.setType("message/rfc822");

	    PackageManager pm = getPackageManager();
	    Intent sendIntent = new Intent(Intent.ACTION_SEND);     
	    sendIntent.setType("text/plain");


	    Intent openInChooser = Intent.createChooser(emailIntent, res.getString(R.string.share_title));

	    List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
	    List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();        
	    for (int i = 0; i < resInfo.size(); i++) {
	        // Extract the label, append it, and repackage it in a LabeledIntent
	        ResolveInfo ri = resInfo.get(i);
	        String packageName = ri.activityInfo.packageName;
	        if(packageName.contains("android.email")) {
	            emailIntent.setPackage(packageName);
	        } else if(packageName.contains("twitter")     //twitter oficial app
	        		|| packageName.contains("orca")       //facebook messenger
	        		|| packageName.contains("android.gm") //gmail
	        		|| packageName.contains("whatsapp"))  //whatsapp
	        {
	            Intent intent = new Intent();
	            intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
	            intent.setAction(Intent.ACTION_SEND);
	            intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, message);
	            if(packageName.contains("android.gm")) {           
	                intent.setType("message/rfc822");
	                intent.putExtra(Intent.EXTRA_SUBJECT, email_subject);
	            }
	            intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
	        }
	    }

	    // convert intentList to array
	    LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

	    openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
	    startActivity(openInChooser);       
	}
	
	Rect rect;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ViewGroup.MarginLayoutParams vlp = (MarginLayoutParams) powerMenu.getLayoutParams();
		float userX = event.getRawX();
		float userY = event.getRawY();
		float circleCenterX = vlp.leftMargin + powerMenu.getWidth()/2;
		float circleCenterY = vlp.topMargin + powerMenu.getHeight()/2;
		float radi = powerMenu.getWidth()/2;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
			if(v == shareButton){				
				shareButton.setImageResource(R.drawable.sharebutton_pressed);
			}else if(v == replayButton){
				replayButton.setImageResource(R.drawable.replaybutton_pressed);
			}else if(v == leaderboardsButton){
				leaderboardsButton.setImageResource(R.drawable.gamebutton_pressed);
			}else if(v == buyPoints){
				buyPoints.setImageResource(R.drawable.buypoints_pressed);
			}
			break;
		case MotionEvent.ACTION_UP:
			Boolean isOn = true;
			if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                isOn = false;
            }
			if(v == shareButton){
				if(isOn){
					share();
				}
				shareButton.setImageResource(R.drawable.sharebutton);
			}else if(v == replayButton){
				if(isOn){
					SRGameManager.getInstace().resetGame();
					Intent intent = new Intent(SRScoreActivity.this, SRGameActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.activity_sendleft_in, R.anim.activity_sendleft_out);
					shouldDataBeSended = false;
					finish();
				}
				replayButton.setImageResource(R.drawable.replaybutton);
			}else if(v == leaderboardsButton){
				if(isOn){
					if(!mGameHelper.isSignedIn()){
						//start the sign-in flow
						mGameHelper.beginUserInitiatedSignIn();
					}else{
						SRPopUpMenu menu = new SRPopUpMenu(this);
						menu.listview.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								switch (position) {
								case 0:
									//Achievements
									startAchievementsActivity();
									break;

								case 1:
									//Leaderboards
									startLeaderboardActivity();
									break;
								default:
									break;
								}
							}
						});
						menu.show();
					}
				}
				leaderboardsButton.setImageResource(R.drawable.gamebutton);
			}else if(v == buyPoints){
				if(isOn){
					color.setBackgroundColor(Color.YELLOW);
					pointsMenu.openPointsMenu();
					color.setVisibility(View.VISIBLE);
					pointsMenu.startAnimation(menu_in);
					color.startAnimation(fons_in);
				}
				buyPoints.setImageResource(R.drawable.buypoints);
			}else if(v == needHelpText){
				if(isOn){
					Intent intent = new Intent(SRScoreActivity.this, SRStartActivity.class);
					intent.putExtra("showTutorial", true);
					startActivity(intent);
					finish();
				}
			}
			
			if(Math.sqrt(Math.pow(userX-circleCenterX, 2)+Math.pow(userY-circleCenterY, 2)) > radi && v != buyPoints){
				if(powerMenu.getVisibility() == View.VISIBLE){
					closeMenu(0);
				} else if(pointsMenu.getVisibility() == View.VISIBLE){
					closeMenu(1);
				}
			}
			break;
		default:
			break;
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!mIAHelper.handleActivityResult(requestCode, resultCode, data)) {     
			super.onActivityResult(requestCode, resultCode, data);
	    }		
		mGameHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onPause() {
		if (adView != null) {
		      adView.pause();
		}
		super.onPause();
		SRGameManager.getInstace().saveAllData();
	}

	@Override
	public void onDestroy() {
		if (adView != null) {
		      adView.destroy();
		}		
		super.onDestroy();
		if (mIAHelper != null) mIAHelper.dispose();
		mIAHelper = null;
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    if (adView != null) {
	      adView.resume();
	    }
	}	
	
	@Override
	public void onBackPressed() {
		if(powerMenu.getVisibility() == View.VISIBLE){
			closeMenu(0);
		}
		if(pointsMenu.getVisibility() == View.VISIBLE){
			closeMenu(1);
		}
		if(powerMenu.getVisibility() != View.VISIBLE && pointsMenu.getVisibility() != View.VISIBLE){
			finish();
		}
	}
	
	/**
	 * Class that allows passing the variables to the server, and when the variables should change,
	 * the server returns the new ones.
	 *  */
	private class serverVariables extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			//TODO
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post;
			if(!SRUserData.getInstace().isServerInit()){
				post = new HttpPost("http://www.fernandezmir.com/spin/api/new_user.php");
				Log.d("REQUEST", "isServerInit: false");
			}else{
				post = new HttpPost("http://www.fernandezmir.com/spin/api/old_user.php");
				Log.d("REQUEST", "isServerInit: true");
			}
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
						
			try {
				StringEntity se = new StringEntity("go=1&initial_difficulty="+SRUserData.getInstace().getInitialDifficulty()
													+"&difficulty_increase="+SRUserData.getInstace().getDifficultyIncrease()
													+"&margin_error="+SRUserData.getInstace().getErrorMargin()
													+"&shift_time="+SRUserData.getInstace().getShiftTime());
				
				Log.d("REQUEST", "go=1&initial_difficulty="+SRUserData.getInstace().getInitialDifficulty()
						+"&difficulty_increase="+SRUserData.getInstace().getDifficultyIncrease()
						+"&margin_error="+SRUserData.getInstace().getErrorMargin()
						+"&shift_time="+SRUserData.getInstace().getShiftTime());
				
				post.setEntity(se);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			//sending the request
			HttpResponse response = null;
			String answer = "null";
 			InputStream content = null;
	 		try {
	 			response = client.execute(post);
				content = response.getEntity().getContent();
	 			if(content != null){
	 				answer = getStringFromInputStream(content);
	 			}
	 		} catch (ClientProtocolException e) {
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			e.printStackTrace();
	 		} catch (IllegalStateException e) {
				e.printStackTrace();
			}
	 		 		
 			Log.d("REQUEST", "Answer: "+answer);
	 		
			return answer;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result); 			
			//parsing the answer and saving int when necessary
	 		JSONObject mJsonAnswer;
	 		try {
				mJsonAnswer = new JSONObject(result);
				if(!SRUserData.getInstace().isServerInit()){
		 			if(mJsonAnswer.getBoolean("success")){
		 				SRUserData.getInstace().setServerInit(true);
		 			}		 			
		 		}else{
		 			SRUserData.getInstace().saveStudyParameters(
		 					(int)mJsonAnswer.getDouble("new_initial_difficulty"),
		 					(float)mJsonAnswer.getDouble("new_difficulty_increase"),
		 					(float)mJsonAnswer.getDouble("new_margin_error"),
		 					(float)mJsonAnswer.getDouble("new_shift_time"));
		 		}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private static String getStringFromInputStream(InputStream is) throws IOException {		 
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = "";
		String result = "";
		while((line = br.readLine()) != null){
			result += line;
		}
		is.close();
		return result; 
	}

	//-----------------------------------------------------------------------
	//The following classes and methods are related to the play game services
	//-----------------------------------------------------------------------
	/** This class is used as a support class for saving the achievements
	 * that have been acomplished, but that haven't been uploaded to the server*/
	class AccomplishmentsOutbox {
        boolean mScore100 = false;
        boolean mScore300 = false;
        boolean mScore500 = false;
        boolean mScore1000 = false;
        boolean mScore2000 = false;
        boolean mScore3000 = false;
        boolean mScore5000 = false;
        boolean mScore10000 = false;
        int mScore = -1;
        
        //Preference name
    	private static final String PREFS_NAME = "SpinRevolutionAchievements";
    	//keys for saving data in sharedPreferences
    	private static final String SCORE_100_KEY = "score100";
    	private static final String SCORE_300_KEY = "score300";
    	private static final String SCORE_500_KEY = "score500";
    	private static final String SCORE_1000_KEY = "score1000";
    	private static final String SCORE_2000_KEY = "score2000";
    	private static final String SCORE_3000_KEY = "score3000";
    	private static final String SCORE_5000_KEY = "score5000";
    	private static final String SCORE_10000_KEY = "score10000";
    	
    	SharedPreferences prefs = SRScoreActivity.this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        public void saveLocal(Context context) {
            prefs.edit().putBoolean(SCORE_100_KEY, mScore100);
            prefs.edit().putBoolean(SCORE_300_KEY, mScore300);
            prefs.edit().putBoolean(SCORE_500_KEY, mScore500);
            prefs.edit().putBoolean(SCORE_1000_KEY, mScore1000);
            prefs.edit().putBoolean(SCORE_2000_KEY, mScore2000);
            prefs.edit().putBoolean(SCORE_3000_KEY, mScore3000);
            prefs.edit().putBoolean(SCORE_5000_KEY, mScore5000);
            prefs.edit().putBoolean(SCORE_10000_KEY, mScore10000);
            prefs.edit().commit();
        }

        public void loadLocal(Context context) {
        	mScore100 = prefs.getBoolean(SCORE_100_KEY, mScore100);
        	mScore300 = prefs.getBoolean(SCORE_300_KEY, mScore300);
        	mScore500 = prefs.getBoolean(SCORE_500_KEY, mScore500);
        	mScore1000 = prefs.getBoolean(SCORE_1000_KEY, mScore1000);
        	mScore2000 = prefs.getBoolean(SCORE_2000_KEY, mScore2000);
        	mScore3000 = prefs.getBoolean(SCORE_3000_KEY, mScore3000);
        	mScore5000 = prefs.getBoolean(SCORE_5000_KEY, mScore5000);
        	mScore10000 = prefs.getBoolean(SCORE_10000_KEY, mScore10000);
        }
    }
	
	public void pushAcomplishments(){
		if (!mGameHelper.isSignedIn()) {
            // can't push to the cloud, so save locally
            mOutbox.saveLocal(this);
            return;
        }
		if (mOutbox.mScore100) {
            Games.Achievements.unlock(mGameHelper.getApiClient(), getString(R.string.achievement_score100));
            //mOutbox.mScore100 = false;
        }
        if (mOutbox.mScore300) {
            Games.Achievements.unlock(mGameHelper.getApiClient(), getString(R.string.achievement_score300));
            //mOutbox.mScore300 = false;
        }
        if (mOutbox.mScore500) {
            Games.Achievements.unlock(mGameHelper.getApiClient(), getString(R.string.achievement_score500));
            //mOutbox.mScore500 = false;
        }
        if (mOutbox.mScore1000) {
            Games.Achievements.unlock(mGameHelper.getApiClient(), getString(R.string.achievement_score1000));
            //mOutbox.mScore1000 = false;
        }
        if (mOutbox.mScore2000) {
            Games.Achievements.unlock(mGameHelper.getApiClient(), getString(R.string.achievement_score2000));
            //mOutbox.mScore2000 = false;
        }
        if (mOutbox.mScore3000) {
            Games.Achievements.unlock(mGameHelper.getApiClient(), getString(R.string.achievement_score3000));
            //mOutbox.mScore3000 = false;
        }
        if (mOutbox.mScore5000) {
            Games.Achievements.unlock(mGameHelper.getApiClient(), getString(R.string.achievement_score5000));
            //mOutbox.mScore5000 = false;
        }
        if (mOutbox.mScore10000) {
            Games.Achievements.unlock(mGameHelper.getApiClient(), getString(R.string.achievement_score10000));
            //mOutbox.mScore10000 = false;
        }
        if (mOutbox.mScore >= 0) {
            Games.Leaderboards.submitScore(mGameHelper.getApiClient(), getString(R.string.leaderboard), mOutbox.mScore);
            //mOutbox.mScore = -1;
        }
        Games.Achievements.increment(mGameHelper.getApiClient(), getString(R.string.achievement_games5), 1);
        Games.Achievements.increment(mGameHelper.getApiClient(), getString(R.string.achievement_games10), 1);
        Games.Achievements.increment(mGameHelper.getApiClient(), getString(R.string.achievement_games50), 1);
        Games.Achievements.increment(mGameHelper.getApiClient(), getString(R.string.achievement_games100), 1);
        Games.Achievements.increment(mGameHelper.getApiClient(), getString(R.string.achievement_games500), 1);
        Games.Achievements.increment(mGameHelper.getApiClient(), getString(R.string.achievement_games1000), 1);
        mOutbox.saveLocal(this);
	}
	
	public void unlockAchievement(int achievementId, String fallbackString) {
        if (mGameHelper.isSignedIn()) {
            Games.Achievements.unlock(mGameHelper.getApiClient(), getString(achievementId));
            Log.d("Achiv", "achievementUnlucked "+achievementId);
        }
    }
	
	public void checkForAchievements(int finalScore){
		if(finalScore >= 100){
			mOutbox.mScore100 = true;
		} 
		if(finalScore >= 300) {
			mOutbox.mScore300 = true;
		} 
		if(finalScore >= 500) {
			mOutbox.mScore500 = true;
		} 
		if(finalScore >= 1000) {
			mOutbox.mScore1000 = true;
		} 
		if(finalScore >= 2000) {
			mOutbox.mScore2000 = true;
		} 
		if(finalScore >= 3000) {
			mOutbox.mScore3000 = true;
		} 
		if(finalScore >= 5000) {
			mOutbox.mScore5000 = true;
		} 
		if(finalScore >= 10000) {
			mOutbox.mScore10000 = true;
		}
	}
	
	/**
     * Update leaderboards with the user's score.
     *
     * @param finalScore The score the user got.
     */
    void updateLeaderboards(int finalScore) {
        mOutbox.mScore = finalScore;
    }
    
    public void startAchievementsActivity() {
    	startActivityForResult(Games.Achievements.getAchievementsIntent(mGameHelper.getApiClient()), RC_UNUSED);
    	Log.d("", "Starting achievements");
    }

    public void startLeaderboardActivity() {
    	startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGameHelper.getApiClient()), RC_UNUSED);
    	Log.d("", "Starting leaderboards");
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        mGameHelper.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGameHelper.onStop();
		if(shouldDataBeSended){
			mixpanel.flush();
			Log.d("Analytics", "Data sended");
		}
    }    
}
