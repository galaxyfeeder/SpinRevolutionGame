package com.tomorrowdev.spinrev;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.LayoutGameActivity;
import org.json.JSONException;
import org.json.JSONObject;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.tomorrowdev.spinrev.views.SRCircleLives;
import com.tomorrowdev.spinrev.views.SRPUHeader;
import com.tomorrowdev.spinrev.views.SRPUHeader.OnComboPUEventListener;
import com.tomorrowdev.spinrev.views.SRPUHeader.OnExtraPUEventListener;
import com.tomorrowdev.spinrev.views.SRPUHeader.OnImmuPUEventListener;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class SRGameActivity extends LayoutGameActivity{

	int SCREEN_WIDTH;
	int SCREEN_HEIGHT;
	float DENSITY;
	
	float positionSpinnerCenterX, positionSpinnerCenterY;
	int countDown, combo, comboMultiplier;
	Boolean firstTouch, isTouching, shouldSpin, shouldDecreaseLife, shouldChangeMode;
	float colorTime, comboTime, shiftTime, shiftTimeMax; 
	float thetaSpin, thetaUser, colorAngle;
	int color = 0;
	float COLOR_MARGIN;
	boolean mStopScoreHandler = false;
	private boolean comboPUused = false;
	
	RelativeLayout container;
	TextView scoreText, countDownTV, comboTV;
	SRPUHeader header;
	SRCircleLives lives;
	private final Handler mHandler = new Handler();
	Resources res;

	AudioManager audio;
	private SoundPool soundPool;
	private int soundDos, soundQuatre, soundSis, soundVuit, soundFail;
	private boolean soundPoolLoaded = false;
	
	Scene gameScene;	
	BitmapTextureAtlas spinnerTA;
	ITextureRegion spinnerTR;
	Sprite sSpinner;
	
	//mixpanel
	MixpanelAPI mixpanel;
	public static final String MIXPANEL_TOKEN = "e8429217f5a5aa0dec4a893d94960c2d";
	Boolean shouldDataBeSended = true;

	@Override
	public EngineOptions onCreateEngineOptions() {	
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		SCREEN_WIDTH = displaymetrics.widthPixels;
		SCREEN_HEIGHT = displaymetrics.heightPixels;
		
		DENSITY = displaymetrics.density;
		
		firstTouch = false;
		isTouching = false;
		shouldSpin = false;
		shouldDecreaseLife = false;
		shouldChangeMode = true;
		comboMultiplier = 1;
		
		colorTime = 0;
		comboTime = 0;
		shiftTime = 0;

		Camera mCamera = new Camera(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(
						SCREEN_WIDTH, SCREEN_HEIGHT), mCamera);
		return options;
	}
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		
		//Load Spinner resources
		loadGfx();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	private void loadGfx() {
		spinnerTA = new BitmapTextureAtlas(getTextureManager(), 2048, 2048);
		spinnerTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spinnerTA, this, "spinner.png", 0, 0);
		spinnerTA.load();
	}
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {

		gameScene = new Scene();
		gameScene.setBackground(new Background(255, 255, 255));
		gameScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			
			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent event) {
				if (event.getAction() == TouchEvent.ACTION_UP){
					isTouching = false;
				}
				return true;
			}
		});
		
		pOnCreateSceneCallback.onCreateSceneFinished(gameScene);
	}
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {

		//http://www.matim-dev.com/handling-touch-events.html
		
		sSpinner = new Sprite((int)(SCREEN_WIDTH*0.5), (int)(SCREEN_HEIGHT*0.3), spinnerTR, this.mEngine.getVertexBufferObjectManager()){
			@Override
		    public boolean onAreaTouched(TouchEvent event, float X, float Y) {
				switch (event.getAction()) {
				case TouchEvent.ACTION_DOWN:
				if (firstTouch){
					isTouching = true;
					thetaUser = angleFromEvent(event);
				}else{
					runOnUiThread(new Runnable() {							
						@Override
						public void run() {
							new CountDownTimer(3000, 100) {
								int secondsLeft = 0;
								@Override
								public void onTick(long ms) {
									if (Math.round((float)ms / 1000.0f) != secondsLeft){
							             secondsLeft = Math.round((float)ms / 1000.0f);
							             
							             countDownTV.setTextSize((int) (SCREEN_WIDTH/DENSITY * 0.3));
							             RelativeLayout.LayoutParams countDown_params = new RelativeLayout.LayoutParams(SCREEN_WIDTH, LayoutParams.WRAP_CONTENT);
							     	     countDown_params.setMargins(0, (int)(SCREEN_HEIGHT*0.57), 0, 0);
							     	     countDownTV.setLayoutParams(countDown_params);
							             
							             if(secondsLeft > 1){
							            	 countDownTV.setText(secondsLeft+"");
							             } else if(secondsLeft == 1){
							            	 countDownTV.setText(secondsLeft+"");
							            	 //shouldSpin = true;
							             } else if(secondsLeft == 0){
							            	 countDownTV.setText(res.getString(R.string.go));
							            	 shouldSpin = true;
							             }
							         }
								}
								@Override
								public void onFinish() {
									countDownTV.setText(res.getString(R.string.go));
					            	shouldDecreaseLife = true;								            	 
					            	container.removeView(countDownTV);										
								}
							}.start();								
						}
					});			
					firstTouch = true;
					}
					break;
				case TouchEvent.ACTION_MOVE:
					firstTouch = true;
					isTouching = true;
					thetaUser = angleFromEvent(event);
					break;
				case TouchEvent.ACTION_UP:
					isTouching = false;
					break;
				case TouchEvent.ACTION_OUTSIDE:
					isTouching = false;
					break;
				default:
					break;
				}
		        return true;
		    };
		};
		float SPActualSize = sSpinner.getWidth();
		float SPNeededSize = (float) (0.8 * SCREEN_WIDTH);

		sSpinner.setScale(SPNeededSize / SPActualSize);
				
		positionSpinnerCenterX = sSpinner.getX();
		positionSpinnerCenterY = sSpinner.getY();
		
		gameScene.registerUpdateHandler(new IUpdateHandler() {			
			@Override
			public void reset() {		
			}			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				update(pSecondsElapsed);
			}
		});
		
		gameScene.registerTouchArea(sSpinner);
		gameScene.attachChild(sSpinner);
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				appInit();
			}
		});
	}

	private void appInit() {
		//getting the resources for setting strings
		res = getResources();
		
		//vibrate
		audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		//set a/b testing variables
		COLOR_MARGIN = SRUserData.getInstace().getErrorMargin();
		shiftTimeMax = SRUserData.getInstace().getShiftTime();
				
		Typeface Helveticaneue_light = Typeface.createFromAsset(getAssets(), "fonts/Helveticaneue-light.ttf");
		
		container = new RelativeLayout(this);
		container = (RelativeLayout) findViewById(R.id.mainContainer);
		
	  	int scoreWidth = SCREEN_WIDTH;
	  	int scoreHeight = (int) (SCREEN_WIDTH/DENSITY * 0.18);
	  	int scoreX = (int)(0.5 * SCREEN_WIDTH)-scoreWidth/2; // = 0 ?
	  	int scoreY = (int)(0.13* SCREEN_HEIGHT); //-scoreHeight/2
	  	
	  	//score label
	    scoreText = new TextView(this);
	    scoreText.setText("0");
	    RelativeLayout.LayoutParams scoreText_params = new RelativeLayout.LayoutParams(scoreWidth, LayoutParams.WRAP_CONTENT);
	    scoreText_params.setMargins(scoreX, scoreY, 0, 0);
	    scoreText.setLayoutParams(scoreText_params);
	    scoreText.setTextColor(Color.GRAY);
	    scoreText.setTextSize(scoreHeight);
	    scoreText.setGravity(Gravity.CENTER);
	    scoreText.setTypeface(Helveticaneue_light);
	    container.addView(scoreText);
	    
	    //header
	    int headerHeight = (int)(SCREEN_WIDTH*0.18);
	    header = new SRPUHeader(this);
	    header.setSize(SCREEN_WIDTH, headerHeight, DENSITY);
	    RelativeLayout.LayoutParams header_params = new RelativeLayout.LayoutParams(SCREEN_WIDTH, headerHeight);
	    header_params.setMargins(0, 0, 0, 0);
	    header.setLayoutParams(header_params);
	    header.setExtraPUEventListener(new OnExtraPUEventListener() {			
			@Override
			public void onEvent() {
				if(SRGameManager.getInstace().getExtraRemaining() > 0 && SRGameManager.getInstace().getLives() < 5){
					int extraUsed = SRGameManager.getInstace().getExtraUsed();
					SRGameManager.getInstace().setExtraUsed(extraUsed++);
					int extraRemaining = SRGameManager.getInstace().getExtraRemaining();
					SRGameManager.getInstace().setExtraRemaining(extraRemaining-1);
					header.setExtraText(""+SRGameManager.getInstace().getExtraRemaining());
					SRGameManager.getInstace().setLives(SRGameManager.getInstace().getLives()+1);
					lives.addLife();								
				}
			}
		});
	    header.setComboPUEventListener(new OnComboPUEventListener() {			
			@Override
			public void onEvent() {
				if(SRGameManager.getInstace().getComboRemaining() > 0 && shouldSpin
						&& comboMultiplier == 1){
					int comboUsed = SRGameManager.getInstace().getComboUsed();
					SRGameManager.getInstace().setComboUsed(comboUsed++);
					int comboRemaining = SRGameManager.getInstace().getComboRemaining();
					SRGameManager.getInstace().setComboRemaining(comboRemaining-1);
					header.setComboText(""+SRGameManager.getInstace().getComboRemaining());
					
					header.showComboProgress(true);
					comboMultiplier = 5;
					comboPUused = false;
					
					//timer that updates the progress circle
					new CountDownTimer(5000, 50) {
						int progress = 0;
						
						@Override
						public void onTick(long ms) {
							int time = (int) (5000 - ms);
							progress = (int)(time * 0.072); // 0.072 = 1/5000*360
							header.setComboProgress(progress);
						}
						@Override
						public void onFinish() {
							header.showComboProgress(false);
							comboMultiplier = 1;
						}
					}.start();
				}
			}
		});
	    header.setImmuPUEventListener(new OnImmuPUEventListener() {			
			@Override
			public void onEvent() {
				if(SRGameManager.getInstace().getImmuRemaining() > 0 && shouldDecreaseLife
						&& shouldSpin){
					int immuUsed = SRGameManager.getInstace().getImmuUsed();
					SRGameManager.getInstace().setImmuUsed(immuUsed++);
					int immuRemaining = SRGameManager.getInstace().getImmuRemaining();
					SRGameManager.getInstace().setImmuRemaining(immuRemaining-1);
					header.setImmuText(""+SRGameManager.getInstace().getImmuRemaining());
					header.showImmuProgress(true);
					shouldDecreaseLife = false;
					//timer that updates the progress circle
					new CountDownTimer(5000, 50) {
						int progress = 0;
						
						@Override
						public void onTick(long ms) {
							int time = (int) (5000 - ms);
							progress = (int)(time * 0.072); // 0.072 = 1/5000*360
							header.setImmuProgress(progress);
						}
						@Override
						public void onFinish() {
							header.showImmuProgress(false);
							shouldDecreaseLife = true;
						}
					}.start();
				}
			}
		});
	    header.setExtraText(""+SRGameManager.getInstace().getExtraRemaining());
	    header.setComboText(""+SRGameManager.getInstace().getComboRemaining());
	    header.setImmuText(""+SRGameManager.getInstace().getImmuRemaining());
	    container.addView(header);
	    
	    //lives
	    lives = new SRCircleLives(this);
	    lives.setSize((int)(SCREEN_WIDTH*0.8), (int)(SCREEN_WIDTH/6));
	    RelativeLayout.LayoutParams lives_params = new RelativeLayout.LayoutParams((int)(SCREEN_WIDTH*0.8), (int)(SCREEN_WIDTH/6));
	    lives_params.setMargins((int)(SCREEN_WIDTH*0.1), (int)(SCREEN_HEIGHT*0.285), 0, 0);
	    lives.setLayoutParams(lives_params);
	    container.addView(lives);
	    
	    //countDown textView
	    countDownTV = new TextView(this);
	    RelativeLayout.LayoutParams countDown_params = new RelativeLayout.LayoutParams(SCREEN_WIDTH, LayoutParams.WRAP_CONTENT);
	    countDown_params.setMargins(0, (int)(SCREEN_HEIGHT*0.67), 0, 0);
	    countDownTV.setLayoutParams(countDown_params);
	    countDownTV.setTextSize((int) (SCREEN_WIDTH/DENSITY * 0.1));
	    countDownTV.setGravity(Gravity.CENTER);
	    countDownTV.setText(res.getString(R.string.touch_to_start));
	    countDownTV.setTextColor(Color.WHITE);
	    countDownTV.setTypeface(Helveticaneue_light);
	    container.addView(countDownTV);
	    
	    int comboTV_width = SCREEN_WIDTH/5;
	  	int comboTV_size = (int) (SCREEN_WIDTH/DENSITY * 0.12);
	  	int comboTV_y = (int)(0.155* SCREEN_HEIGHT);
	  	int comboTV_x = (int)(0.8* SCREEN_WIDTH);
	    
	    
	    //combo textview for showing x2, x4, x8
	    comboTV = new TextView(this);
	    RelativeLayout.LayoutParams comboTV_params = new RelativeLayout.LayoutParams(comboTV_width, LayoutParams.WRAP_CONTENT);
	    comboTV_params.setMargins(comboTV_x, comboTV_y, 0, 0);
	    comboTV.setLayoutParams(comboTV_params);
	    comboTV.setTextSize(comboTV_size);
	    comboTV.setGravity(Gravity.CENTER);
	    comboTV.setTypeface(Helveticaneue_light);
	    container.addView(comboTV);
	    
	    //init mixpanel
	    mixpanel = MixpanelAPI.getInstance(this, MIXPANEL_TOKEN);
	    String device_uuid = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
	    Log.d("UUID", ""+device_uuid);
	    mixpanel.identify(device_uuid);
	    mixpanel.getPeople().identify(device_uuid);
	    mixpanel.getPeople().initPushHandling("791553235288");
	    //user data
	    JSONObject props = new JSONObject();
	    try {
			props.put("extraPU", SRGameManager.getInstace().getExtraRemaining());
			props.put("immuPU", SRGameManager.getInstace().getImmuRemaining());
			props.put("comboPU", SRGameManager.getInstace().getComboRemaining());
			props.put("walletPoints", SRGameManager.getInstace().getTotalPoints());
			props.put("everBought", SRGameManager.getInstace().getEverBought());
			props.put("bestScore", SRGameManager.getInstace().getBestScore());
			props.put("answeredSurvey", SRUserData.getInstace().isSurveyDone() ? 1 : 0);
			props.put("gamesPlayed", SRGameManager.getInstace().getGamesPlayed());
			props.put("initialDifficulty", SRUserData.getInstace().getInitialDifficulty());
			props.put("difficultyIncrease", SRUserData.getInstace().getDifficultyIncrease());
			props.put("shiftTime", SRUserData.getInstace().getShiftTime());
			props.put("errorMargin", SRUserData.getInstace().getErrorMargin());
			try {
				props.put("Android App Version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    //mixpanel.registerSuperProperties(props);
	    mixpanel.getPeople().set(props);
	    
	    //handler that update the score every 30 ms
	    Runnable runnable = new Runnable() {
	        @Override
	        public void run() {
	            if (!mStopScoreHandler) {
	            	scoreText.setText(""+SRGameManager.getInstace().getScore());
	                mHandler.postDelayed(this, 30);
	            }
	        }
	    };
	    mHandler.post(runnable);
	    
	    //SoundPool
	    this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// Load the sound
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				soundPoolLoaded = true;
			}
		});
		soundDos = soundPool.load(this, R.raw.dos, 1);
		soundQuatre = soundPool.load(this, R.raw.quatre, 1);
		soundSis = soundPool.load(this, R.raw.sis, 1);
		soundVuit = soundPool.load(this, R.raw.vuit, 1);
		soundFail = soundPool.load(this, R.raw.fail, 1);
	}
	
	/**	
	 * @param deltaTime
	 * 
	 * Called every frame
	 */
	public void update(float deltaTime){
		//Comprova si ja s'ha clickat per primer cop
		if(shouldSpin){
			//Comprova si s'ha de canviar la funcio
			if(shouldChangeMode){
				if(color==0){
					color = lives.nextColor();
					colorAngle = colorAngleFromColor(color);
				}
				SRSpinnerModifier spMod = new SRSpinnerModifier(this);
				spMod.randomModifier(sSpinner);
				shouldChangeMode = false;
				
			//en el cas que no s'hagi saltat de color, comprova si per temps hauria de saltar
			}else if(colorTime >= 7){
				shouldChangeMode = true;
				colorTime = 0;
			}
			
			thetaSpin = (float) (sSpinner.getRotation());
			thetaSpin = (float) (thetaSpin < 0 ? thetaSpin + 360 : thetaSpin);
			thetaSpin = (float) (thetaSpin % 360);
			thetaSpin = (float) (thetaSpin * 2 * Math.PI / 360);
			thetaSpin = (float) (thetaSpin < 0 ? thetaSpin + 2*Math.PI : thetaSpin);
			thetaSpin = (float) (2 * Math.PI - thetaSpin);
			thetaSpin = (float) (thetaSpin + Math.PI / 2);
			thetaSpin = (float) (thetaSpin % (2 * Math.PI));
			
			//Comprova si estas tocant l'spinner, mirar el onTouch
			if(isTouching){ 
				
				color = lives.nextColor();
				colorAngle = colorAngleFromColor(color);
				
				//comprova si estas en el color que et toca estar
				if((COLOR_MARGIN > (Math.abs(thetaUser - (thetaSpin + colorAngle) % (2*Math.PI)))) ||
				  (COLOR_MARGIN > (2*Math.PI)-(Math.abs(thetaUser - (thetaSpin + colorAngle) % (2*Math.PI))))){
					
					//combo
					comboTime += deltaTime;
					
					if (comboTime > 12 && combo == 6)
	                {
	                    combo = 8;
	                    runOnUiThread(new Runnable() {							
							@Override
							public void run() {
			                    comboTV.setText("x8");
			                    comboTV.setTextColor(Color.argb(255, 255, 0, 255));
			                    float actualVolume = (float) audio.getStreamVolume(AudioManager.STREAM_MUSIC);
								float maxVolume = (float) audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
								float volume = actualVolume / maxVolume;
								if (soundPoolLoaded) {
									soundPool.play(soundVuit, volume, volume, 1, 0, 1f);
								}
							}
						});
	                }
	                else if (comboTime > 8 && combo == 4)
	                {
	                    combo = 6;
	                    runOnUiThread(new Runnable() {							
							@Override
							public void run() {
			                    comboTV.setText("x6");
			                    comboTV.setTextColor(Color.argb(255, 30, 224, 0));
			                    float actualVolume = (float) audio.getStreamVolume(AudioManager.STREAM_MUSIC);
								float maxVolume = (float) audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
								float volume = actualVolume / maxVolume;
								if (soundPoolLoaded) {
									soundPool.play(soundSis, volume, volume, 1, 0, 1f);
								}
							}
						});
	                }
	                else if (comboTime > 5 && combo == 2)
	                {
	                    combo = 4;
	                    runOnUiThread(new Runnable() {							
							@Override
							public void run() {
			                    comboTV.setText("x4");
			                    comboTV.setTextColor(Color.argb(255, 255, 0, 0));
			                    float actualVolume = (float) audio.getStreamVolume(AudioManager.STREAM_MUSIC);
								float maxVolume = (float) audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
								float volume = actualVolume / maxVolume;
								if (soundPoolLoaded) {
									soundPool.play(soundQuatre, volume, volume, 1, 0, 1f);
								}
							}
						});
	                }
	                else if (comboTime > 2 && combo == 1)
	                {
	                    combo = 2;
	                    runOnUiThread(new Runnable() {							
							@Override
							public void run() {								
								comboTV.setText("x2");
					            comboTV.setTextColor(Color.argb(255, 0, 255, 255));
					            Animation combo_in = AnimationUtils.loadAnimation(SRGameActivity.this, R.anim.combo_in);
			                    comboTV.startAnimation(combo_in);
					            float actualVolume = (float) audio.getStreamVolume(AudioManager.STREAM_MUSIC);
								float maxVolume = (float) audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
								float volume = actualVolume / maxVolume;
								if (soundPoolLoaded) {
									soundPool.play(soundDos, volume, volume, 1, 0, 1f);
								}
							}
						});
	                }else if(comboTime < 2){
	                	combo = 1;
	                }
	                
					// Multiply combo
	                if (comboMultiplier == 5 && !comboPUused)
	                {
	                    combo *= comboMultiplier;
	                    comboPUused = true;
	                }
	                
				} else {
					notFollowingSpinner(deltaTime);
				}
			}else{
				notFollowingSpinner(deltaTime);
			}
			SRGameManager.getInstace().incrementScore(combo*0.5f*deltaTime*60);
				
			colorTime += deltaTime;
		}
		SRGameManager.getInstace().incrementPlayedTime(deltaTime);
	}
	private void notFollowingSpinner(float deltaTime) {
		if(shouldDecreaseLife)
			shiftTime += (deltaTime > 1 ? 0 : deltaTime);
		
		if(combo != 0){
			runOnUiThread(new Runnable() {							
				@Override
				public void run() {
					Animation combo_out = AnimationUtils.loadAnimation(SRGameActivity.this, R.anim.combo_out);
					comboTV.startAnimation(combo_out);
				}
			});			
		}
		
		//stop combo
		comboTime = 0;
		combo = 0;
		
		if(shiftTime >= shiftTimeMax){
			SRGameManager.getInstace().lifeLosed();
			
			shiftTime = 0;
			
			if(SRGameManager.getInstace().getLives() != 0){
				runOnUiThread(new Runnable() {				
					@Override
					public void run() {
						lives.nextLife();					
					}
				});	
			}
			
			if(SRGameManager.getInstace().getLives() <= 0){
				//die
				shouldSpin = false;
				
				float actualVolume = (float) audio.getStreamVolume(AudioManager.STREAM_MUSIC);
				float maxVolume = (float) audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				float volume = actualVolume / maxVolume;
				if (soundPoolLoaded) {
					soundPool.play(soundFail, volume, volume, 1, 0, 1f);
				}
				
				if(audio.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
					Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					// Vibrate for 500 milliseconds
					v.vibrate(500);
				}
				
				Intent intent = new Intent(SRGameActivity.this, SRScoreActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_sendright_in, R.anim.activity_sendright_out);
				SRGameManager.getInstace().incrementTotalPoints(SRGameManager.getInstace().getScore());
				if(SRGameManager.getInstace().getScore() > SRGameManager.getInstace().getBestScore()){
					SRGameManager.getInstace().setBestScore(SRGameManager.getInstace().getScore());
				}
				
			 	// Track info to Mixpanel
				JSONObject props = new JSONObject();				
				try {
					props.put("finalPoints", SRGameManager.getInstace().getScore());
					props.put("extraPU", SRGameManager.getInstace().getExtraUsed());
					props.put("immuPU", SRGameManager.getInstace().getImmuUsed());
					props.put("comboPU", SRGameManager.getInstace().getComboUsed());
					props.put("initialDifficulty", SRUserData.getInstace().getInitialDifficulty());
					props.put("difficultyIncrease", SRUserData.getInstace().getDifficultyIncrease());
					props.put("shiftTime", SRUserData.getInstace().getShiftTime());
					props.put("errorMargin", SRUserData.getInstace().getErrorMargin());
				} catch (JSONException e) {
					e.printStackTrace();
				}

				mixpanel.track("Game", props);
				
				shouldDataBeSended = false;				
				SRGameManager.getInstace().saveAllData();
				finish();
				
				return;
			}
		}
	}
	@Override
	protected int getLayoutID() {
		return R.layout.activity_main;
	}
	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.gameSurfaceView;
	}
	public float angleFromEvent(TouchEvent event){
		
		float directorX = event.getX() - positionSpinnerCenterX;
		float directorY = event.getY() - positionSpinnerCenterY;
		
		float angle = (float) Math.acos(directorX / Math.sqrt(Math.pow(directorX, 2)+Math.pow(directorY, 2)));
		angle = (float) (directorY < 0 ? 2 * Math.PI - angle : angle);
		
		return angle;
	}
	public float colorAngleFromColor(int colorInt){
		float radians = 0;
		switch (colorInt) {
		case -65536: radians = 0; break; //red
		case -36864: radians = (float) (Math.PI/3 * 1); break; //orange
		case -256: radians = (float) (Math.PI/3 * 2); break; //yellow
		case -14753762: radians = (float) (Math.PI/3 * 3); break; //green
		case -16776961: radians = (float) (Math.PI/3 * 4); break; //blue
		case -65281: radians = (float) (Math.PI/3 * 5); break; //purple
		default: break;
		}
		return (float) (radians - Math.PI/2);
	}
	
	@Override
	protected void onPause() {
		super.onPause();		
		SRGameManager.getInstace().saveAllData();
	}
	
	@Override
    protected void onStop() {
        super.onStop();
		if(shouldDataBeSended){
			mixpanel.flush();
			Log.d("Analytics", "Data sended");
		}
    }
}
