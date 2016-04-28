package com.tomorrowdev.spinrev;

import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;

public class SRUserData {

	//instance
	private static SRUserData INSTANCE;
	
	//Preference name
	private static final String PREFS_NAME = "SpinRevolutionUD";
	//keys for saving data in sharedPreferences
	private static final String TOTALPOINTS_KEY = "totalPoints";
	private static final String BESTSCORE_KEY = "bestScore";
	private static final String EXTRAPU_KEY = "extraPU";
	private static final String IMMUPU_KEY = "immuPU";
	private static final String COMBOPU_KEY = "comboPU";
	private static final String FIRSTLOAD_KEY = "firstLoad";
	private static final String SURVEY_DONE_KEY = "surveyDone";
	private static final String EVER_BOUGHT_KEY = "everBought";
	private static final String GAMES_PLAYED_KEY = "gamesPlayed";
	private static final String INIT_SERVER_KEY = "serverInit";
	// A/B testing keys
	private static final String INITIAL_DIFFICULTY_KEY = "initialDifficulty";
	private static final String DIFFICULTY_INCREASE_KEY = "difficultyIncrease";
	private static final String SHIFT_TIME_KEY = "shiftTime";
	private static final String ERROR_MARGIN_KEY = "errorMargin";
	
	//Objects for SharedPreferences and editor
	private SharedPreferences mSettings;
	private SharedPreferences.Editor mEditor;
	
	//Variables for setting the data into the Preferences
	private int totalPoints, bestScore, extraPU, immuPU, comboPU, everBought, gamesPlayed;
	private Boolean firstLoad, hasBeenInitialized, surveyDone, serverInit;
	// A/B testing variables
	private int initialDifficulty;
	private float difficultyIncrease, shiftTime, errorMargin;
	
	//Other variables
	// What we consider standard values
	final static int STANDARD_INITIAL_DIFFICULTY = 20;
	final static float STANDARD_DIFFICULTY_INCREASE = 1.5f;
	final static float STANDARD_SHIFT_TIME = 1.6f;
	final static float STANDARD_ERROR_MARGIN = 0.554f;
	// Margins: Max/Min = Standard +/- Margin
	final static int MARGIN_INITIAL_DIFFICULTY = 6;
	final static float MARGIN_DIFFICULTY_INCREASE = 1.5f;
	final static float MARGIN_SHIFT_TIME = 0.3f;
	final static float MARGIN_ERROR_MARGIN = 0.03f;
	
	public SRUserData() {
		hasBeenInitialized = false;
	}
	
	public static SRUserData getInstace(){
		if(INSTANCE == null){
			INSTANCE = new SRUserData();
		}		
		return INSTANCE;
	}
	
	public synchronized void init(Context pContext) {
		if(mSettings == null){
			mSettings = pContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
			mEditor = mSettings.edit();
			
			totalPoints = mSettings.getInt(TOTALPOINTS_KEY, 0);
			bestScore = mSettings.getInt(BESTSCORE_KEY, 0);
			extraPU = mSettings.getInt(EXTRAPU_KEY, 8);
			immuPU = mSettings.getInt(IMMUPU_KEY, 8);
			comboPU = mSettings.getInt(COMBOPU_KEY, 8);
			firstLoad = mSettings.getBoolean(FIRSTLOAD_KEY, true);
			surveyDone = mSettings.getBoolean(SURVEY_DONE_KEY, false);
			everBought = mSettings.getInt(EVER_BOUGHT_KEY, 0);
			gamesPlayed = mSettings.getInt(GAMES_PLAYED_KEY, 0);
			serverInit = mSettings.getBoolean(INIT_SERVER_KEY, false);
			
			initialDifficulty = mSettings.getInt(INITIAL_DIFFICULTY_KEY, createInitialDifficulty());
			difficultyIncrease = mSettings.getFloat(DIFFICULTY_INCREASE_KEY, createDifficultyIncrease());
			shiftTime = mSettings.getFloat(SHIFT_TIME_KEY, createShiftTime());
			errorMargin = mSettings.getFloat(ERROR_MARGIN_KEY, createErrorMargin());
			
			mEditor.putInt(INITIAL_DIFFICULTY_KEY, initialDifficulty);
			mEditor.putFloat(DIFFICULTY_INCREASE_KEY, difficultyIncrease);
			mEditor.putFloat(SHIFT_TIME_KEY, shiftTime);
			mEditor.putFloat(ERROR_MARGIN_KEY, errorMargin);
			mEditor.commit();
			
			hasBeenInitialized = true;
		}
	}
	
	public Boolean hasBeenInitialized() {
		return hasBeenInitialized;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int points) {
		totalPoints = points;
		
		mEditor.putInt(TOTALPOINTS_KEY, totalPoints);
		mEditor.commit();
	}

	public int getBestScore() {
		return bestScore;
	}

	public void setBestScore(int bestScore) {
		this.bestScore = bestScore;
		
		mEditor.putInt(BESTSCORE_KEY, this.bestScore);
		mEditor.commit();
	}

	public int getExtraPU() {
		return extraPU;
	}

	public void setExtraPU(int extraPU) {
		this.extraPU = extraPU;
		
		mEditor.putInt(EXTRAPU_KEY, this.extraPU);
		mEditor.commit();
	}

	public int getImmuPU() {
		return immuPU;
	}

	public void setImmuPU(int immuPU) {
		this.immuPU = immuPU;
		
		mEditor.putInt(IMMUPU_KEY, this.immuPU);
		mEditor.commit();
	}

	public int getComboPU() {
		return comboPU;
	}

	public void setComboPU(int comboPU) {
		this.comboPU = comboPU;
		
		mEditor.putInt(COMBOPU_KEY, this.comboPU);
		mEditor.commit();
	}

	public Boolean isFirstLoad() {
		return firstLoad;
	}

	public void setFirstLoad(Boolean firstLoad) {
		this.firstLoad = firstLoad;		
		mEditor.putBoolean(FIRSTLOAD_KEY, this.firstLoad);
		mEditor.commit();
	}

	public int getInitialDifficulty() {
		//return initialDifficulty;
		//Difficulty changed manually to all games
		return 14;
	}

	public float getDifficultyIncrease() {
		//return difficultyIncrease;
		//Difficulty changed manually to all games
		return 3f;
	}

	public float getShiftTime() {
		//return shiftTime;
		//Difficulty changed manually to all games
		return 1.3f;
	}

	public float getErrorMargin() {
		//return errorMargin;
		//Difficulty changed manually to all games
		return 0.58f;
	}
	
	public Boolean isSurveyDone() {
		return surveyDone;
	}

	public void setSurveyDone(Boolean surveyDone) {
		this.surveyDone = surveyDone;
		mEditor.putBoolean(SURVEY_DONE_KEY, this.surveyDone);
		mEditor.commit();
	}

	public int getEverBought() {
		return everBought;
	}

	public void setEverBought(int everBought) {
		this.everBought = everBought;
		mEditor.putInt(EVER_BOUGHT_KEY, this.everBought);
		mEditor.commit();
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
		mEditor.putInt(GAMES_PLAYED_KEY, this.gamesPlayed);
		mEditor.commit();
	}

	public Boolean isServerInit() {
		return serverInit;
	}

	public void setServerInit(Boolean serverInit) {
		this.serverInit = serverInit;
		mEditor.putBoolean(INIT_SERVER_KEY, this.serverInit);
		mEditor.commit();
	}
	
	public void saveStudyParameters(int initialdifficulty, float difficultyincrease, float marginerror, float shifttime){
		initialDifficulty = initialdifficulty;
		difficultyIncrease = difficultyincrease;
		errorMargin = marginerror;
		shiftTime = shifttime;
		
		mEditor.putInt(INITIAL_DIFFICULTY_KEY, initialDifficulty);
		mEditor.putFloat(DIFFICULTY_INCREASE_KEY, difficultyIncrease);
		mEditor.putFloat(SHIFT_TIME_KEY, shiftTime);
		mEditor.putFloat(ERROR_MARGIN_KEY, errorMargin);
		mEditor.commit();
	}

	private int createInitialDifficulty(){
		return (int) (STANDARD_INITIAL_DIFFICULTY  + getGaussianNumber() * MARGIN_INITIAL_DIFFICULTY);
	}
	
	private float createDifficultyIncrease(){
		return STANDARD_DIFFICULTY_INCREASE + getGaussianNumber() * MARGIN_DIFFICULTY_INCREASE;		
	}
	
	private float createShiftTime(){
		return STANDARD_SHIFT_TIME + getGaussianNumber() * MARGIN_SHIFT_TIME;		
	}
	
	private float createErrorMargin(){
		return STANDARD_ERROR_MARGIN + getGaussianNumber() * MARGIN_ERROR_MARGIN;		
	}
	
	/** 
	 * @return a gaussian number between 1 and -1
	 * */
	public float getGaussianNumber(){
		float gaussian = 0;
	    Random rand = new Random();
	    do {
	    	gaussian = (float) rand.nextGaussian();
		} while (gaussian < -1 || gaussian > 1);
		return gaussian;		
	}
}
