package com.tomorrowdev.spinrev;

import android.content.Context;

public class SRGameManager {

	private static SRGameManager INSTANCE;
	private int lives, totalPoints, bestScore;
	private int extraUsed, immuUsed, comboUsed, extraRemaining, immuRemaining, comboRemaining;
	private int everBought, gamesPlayed;
	private float score, playedTime;
	
	public SRGameManager() {
		score = 0;
		lives = 5;
		
		extraUsed = 0;
		immuUsed = 0;
		comboUsed = 0;
		
		playedTime = 0;
	}
	
	public void init(Context context){
		//init sharedprefs
		SRUserData.getInstace().init(context);
		
		totalPoints = SRUserData.getInstace().getTotalPoints();
		extraRemaining = SRUserData.getInstace().getExtraPU();
		immuRemaining = SRUserData.getInstace().getImmuPU();
		comboRemaining = SRUserData.getInstace().getComboPU();
		bestScore = SRUserData.getInstace().getBestScore();
		everBought = SRUserData.getInstace().getEverBought();
		gamesPlayed = SRUserData.getInstace().getGamesPlayed();
	}
	
	public int getBestScore() {
		return bestScore;
	}

	public void setBestScore(int bestScore) {
		this.bestScore = bestScore;
	}
	
	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	
	public void incrementTotalPoints(int pointsToIncrement){
		this.totalPoints += pointsToIncrement;
	}

	public static SRGameManager getInstace(){
		if(INSTANCE == null){
			INSTANCE = new SRGameManager();
		}		
		return INSTANCE;
	}

	public int getScore() {
		return (int) score;
	}

	public void setScore(float score) {
		this.score = score;
	}
	
	public void incrementScore(float increment){
		this.score += increment;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}
	
	public void lifeLosed(){
		this.lives--;
	}
	
	public void resetGame(){
		score = 0;
		lives = 5;
		
		extraUsed = 0;
		immuUsed = 0;
		comboUsed = 0;
		
		playedTime = 0;
	}
	
	public int getExtraUsed() {
		return extraUsed;
	}

	public void setExtraUsed(int extraUsed) {
		this.extraUsed = extraUsed;
	}

	public int getImmuUsed() {
		return immuUsed;
	}

	public void setImmuUsed(int immuUsed) {
		this.immuUsed = immuUsed;
	}

	public int getComboUsed() {
		return comboUsed;
	}

	public void setComboUsed(int comboUsed) {
		this.comboUsed = comboUsed;
	}

	public float getPlayedTime() {
		return playedTime;
	}

	public void setPlayedTime(float playedTime) {
		this.playedTime = playedTime;
	}

	public void incrementPlayedTime(float deltaToincrement){
		this.playedTime += deltaToincrement;
	}
	
	public int getExtraRemaining() {
		return extraRemaining;
	}

	public void setExtraRemaining(int extraRemaining) {
		this.extraRemaining = extraRemaining;
	}

	public int getImmuRemaining() {
		return immuRemaining;
	}

	public void setImmuRemaining(int immuRemaining) {
		this.immuRemaining = immuRemaining;
	}

	public int getComboRemaining() {
		return comboRemaining;
	}

	public void setComboRemaining(int comboRemaining) {
		this.comboRemaining = comboRemaining;
	}

	public int getEverBought() {
		return everBought;
	}

	public void setEverBought(int everBought) {
		this.everBought = everBought;
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public void incrementGamesPlayed(int increment) {
		this.gamesPlayed += increment;
	}

	public void saveAllData(){
		SRUserData.getInstace().setTotalPoints(totalPoints);
		SRUserData.getInstace().setExtraPU(extraRemaining);
		SRUserData.getInstace().setImmuPU(immuRemaining);
		SRUserData.getInstace().setComboPU(comboRemaining);
		SRUserData.getInstace().setBestScore(bestScore);
		SRUserData.getInstace().setEverBought(everBought);
		SRUserData.getInstace().setGamesPlayed(gamesPlayed);
	}
}
