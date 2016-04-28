package com.tomorrowdev.spinrev;

import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.util.modifier.ease.EaseElasticInOut;
import org.andengine.util.modifier.ease.EaseExponentialInOut;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.EaseSineInOut;

import android.content.Context;

public class SRSpinnerModifier {
	
	final static int F_TIME = 7;
	
	int lastRandom;
	float extraDestination, destination;
	boolean speedPUWorking;
	
	public SRSpinnerModifier(Context context){
		if(!SRUserData.getInstace().hasBeenInitialized())
		SRUserData.getInstace().init(context);
		
        destination      = SRUserData.getInstace().getInitialDifficulty();
        extraDestination = SRUserData.getInstace().getDifficultyIncrease();
        lastRandom  = 5;
	}
	
	public void randomModifier(Entity ent){
		int random = new Random().nextInt(4);
		while(lastRandom == random){
			random = new Random().nextInt(4);
		}
		lastRandom = random;
		
		int randomDestination = new Random().nextInt(2);
		
		destination = Math.abs(destination) + extraDestination;
		destination = randomDestination == 0 ? destination : -destination;
		destination = (float) (destination/2/Math.PI*360);		
		
		RotationModifier[] modifiers = new RotationModifier[4];
		
		modifiers[0] = new RotationModifier(F_TIME, ent.getRotation(), (float) (ent.getRotation() + destination * 0.22 * 0.88), EaseElasticInOut.getInstance());
		modifiers[1] = new RotationModifier(F_TIME, ent.getRotation(), (float) (ent.getRotation() + destination * 1.28 * 0.88), EaseLinear.getInstance());
		modifiers[2] = new RotationModifier(F_TIME, ent.getRotation(), (float) (ent.getRotation() + destination * 1.15 * 0.88), EaseSineInOut.getInstance());
		modifiers[3] = new RotationModifier(F_TIME, ent.getRotation(), (float) (ent.getRotation() + destination * 0.56 * 0.88), EaseExponentialInOut.getInstance());
		
		ent.registerEntityModifier(modifiers[random]);
	}
}
