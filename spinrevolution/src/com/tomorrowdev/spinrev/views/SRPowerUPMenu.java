package com.tomorrowdev.spinrev.views;

import com.tomorrowdev.spinrev.R;
import com.tomorrowdev.spinrev.SRGameManager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

public class SRPowerUPMenu extends RelativeLayout implements OnTouchListener{
	OnBuyEventListener mBuyListener;	
	
	public interface OnBuyEventListener{
		public void onEvent(int type, int quantity);
	}
	
	public void setBuyEventListener(OnBuyEventListener event){
		mBuyListener = event;	
	}
	
	public final static int EXTRA_POWERUP = 0;
	public final static int IMMU_POWERUP = 1;
	public final static int COMBO_POWERUP = 2;
	
	int OBJECT_WIDTH, OBJECT_HEIGHT;
	float DENSITY;
	
	TextView menuTitle, menuInfo, quantityLabel, priceLabel, buyButton;
	ImageView oneMore, oneLess;
	View button;
	Resources res;
	
	int buyQuantity;
	int PUprice = 1000;
	int PUtype;
	
	Context context;
	
	public SRPowerUPMenu(Context context) {
		super(context);
		this.context = context;
		res = context.getResources();
	}
	
	public void setSize(int width, int height, float density){
		OBJECT_WIDTH = width;
		OBJECT_HEIGHT = height;

		DENSITY = density;
		
		buyQuantity = 1;
		
		sharedConstructor(context);
	}

	private void sharedConstructor(Context context) {
		setVisibility(View.INVISIBLE);
		setBackgroundResource(R.drawable.pumenu);
		
		//Typefaces
	    Typeface Helveticaneue_light = Typeface.createFromAsset(context.getAssets(), "fonts/Helveticaneue-light.ttf");
	    Typeface Helveticaneue_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Helveticaneue-bold.ttf");
		
		//linearLayout container
		LinearLayout container = new LinearLayout(context);
		RelativeLayout.LayoutParams container_params = new RelativeLayout.LayoutParams(OBJECT_WIDTH, (int) (OBJECT_HEIGHT*0.76));
	    container.setLayoutParams(container_params);
	    container.setOrientation(LinearLayout.VERTICAL);
	    container.setGravity(Gravity.CENTER);
	    container.setWeightSum(100);
	    addView(container);
		
		//menuTitle
		menuTitle = new TextView(context);
		LinearLayout.LayoutParams menuTitle_params = new LinearLayout.LayoutParams(OBJECT_WIDTH/10*7, 0);
		menuTitle_params.weight = 30;
		menuTitle.setLayoutParams(menuTitle_params);
		menuTitle.setTextSize((int)(OBJECT_WIDTH/DENSITY*0.09));
		menuTitle.setGravity(Gravity.CENTER);
		menuTitle.setTypeface(Helveticaneue_bold);
		container.addView(menuTitle);
		
		//menuInfo		
		menuInfo = new TextView(context);
		LinearLayout.LayoutParams menuInfo_params = new LinearLayout.LayoutParams(OBJECT_WIDTH/10*8, 0);
		menuInfo_params.weight = 26;
		menuInfo.setLayoutParams(menuInfo_params);
		menuInfo.setTextSize((int)(OBJECT_WIDTH/DENSITY*0.07));
		menuInfo.setTextColor(Color.BLACK);
		menuInfo.setGravity(Gravity.CENTER);
		menuInfo.setTypeface(Helveticaneue_light);
		container.addView(menuInfo);
		
		//sum, deduct, number container
		LinearLayout numbersContainer = new LinearLayout(context);
		LinearLayout.LayoutParams numbersContainer_params = new LinearLayout.LayoutParams(OBJECT_WIDTH, 0);
	    numbersContainer_params.weight = 26;
	    numbersContainer.setLayoutParams(numbersContainer_params);
	    numbersContainer.setOrientation(LinearLayout.HORIZONTAL);
	    numbersContainer.setGravity(Gravity.CENTER);
	    container.addView(numbersContainer);
	    
	    int buttonSide = (int)(OBJECT_WIDTH*0.12);
	    
	    //oneLess button
	    oneLess = new ImageView(context);
	    LinearLayout.LayoutParams oneLess_params = new LinearLayout.LayoutParams(buttonSide, buttonSide);
	    oneLess.setLayoutParams(oneLess_params);
	    oneLess.setImageResource(R.drawable.deductpu);
	    oneLess.setOnTouchListener(this);
	    numbersContainer.addView(oneLess);
	    
	    //Text
	    quantityLabel = new TextView(context);
	    LinearLayout.LayoutParams quantityLabel_params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    quantityLabel.setLayoutParams(quantityLabel_params);
	    quantityLabel.setTextSize((int)(OBJECT_WIDTH/DENSITY*0.15));
	    quantityLabel.setText(" "+buyQuantity+" ");
	    quantityLabel.setTextColor(Color.BLACK);
	    quantityLabel.setTypeface(Helveticaneue_light);
	    numbersContainer.addView(quantityLabel);
	    
	    //oneMore button
	    oneMore = new ImageView(context);
	    LinearLayout.LayoutParams oneMore_params = new LinearLayout.LayoutParams(buttonSide, buttonSide);
	    oneMore.setLayoutParams(oneMore_params);
	    oneMore.setImageResource(R.drawable.addpu);
	    oneMore.setOnTouchListener(this);
	    numbersContainer.addView(oneMore);
	    
	    //price label
	    priceLabel = new TextView(context);
	    LinearLayout.LayoutParams priceLabel_params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0);
	    priceLabel_params.weight = 18;
	    priceLabel.setLayoutParams(priceLabel_params);
	    priceLabel.setTextSize((int)(OBJECT_WIDTH/DENSITY*0.09));
	    priceLabel.setTextColor(Color.BLACK);
	    priceLabel.setText("1000 "+res.getString(R.string.points));
	    priceLabel.setGravity(Gravity.CENTER);
	    priceLabel.setTypeface(Helveticaneue_light);
	    container.addView(priceLabel);
	    
	    //View that recognize the onClick on the green part
	 	button = new View(context);
	 	RelativeLayout.LayoutParams button_params = new RelativeLayout.LayoutParams((int) (OBJECT_WIDTH*0.88), (int) (OBJECT_HEIGHT*0.24));
	 	button_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
	 	button_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	 	button.setLayoutParams(button_params);
	 	button.setOnTouchListener(this);
	 	addView(button);
	 	
	 	//buy label
	 	buyButton = new TextView(context);
	 	RelativeLayout.LayoutParams buyButton_params = new RelativeLayout.LayoutParams(OBJECT_WIDTH, (int) (OBJECT_HEIGHT*0.24));
	 	buyButton_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
	 	buyButton_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	 	buyButton.setLayoutParams(buyButton_params);
	 	buyButton.setText("BUY");
	 	buyButton.setTypeface(Helveticaneue_bold);
	 	buyButton.setTextColor(Color.WHITE);
	 	buyButton.setTextSize((int)(OBJECT_WIDTH/DENSITY*0.15));
	 	buyButton.setGravity(Gravity.CENTER);
	 	addView(buyButton);
	}
	
	public void openPowerUpMenu(int type){
		setVisibility(View.VISIBLE);
		buyQuantity = 1;
		quantityLabel.setText(" "+buyQuantity+" ");
		priceLabel.setText(buyQuantity*PUprice+" "+res.getString(R.string.points));
		
		switch (type) {
		case EXTRA_POWERUP:
			menuTitle.setText(res.getString(R.string.extra_title));
			menuTitle.setTextColor(Color.RED);
			
			menuInfo.setText(res.getString(R.string.extra_description));
			
			PUtype = EXTRA_POWERUP;
			break;
		case IMMU_POWERUP:
			menuTitle.setText(res.getString(R.string.immu_title));
			menuTitle.setTextColor(Color.BLUE);
			
			menuInfo.setText(res.getString(R.string.immu_description));
			
			PUtype = IMMU_POWERUP;
			break;		
		case COMBO_POWERUP:
			menuTitle.setText(res.getString(R.string.combo_title));
			menuTitle.setTextColor(Color.rgb(254, 154, 46));
			
			menuInfo.setText(res.getString(R.string.combo_description));
			
			PUtype = COMBO_POWERUP;
			break;
		default:
			break;
		}
	}
	
	public void closePoweUpMenu(){
		setVisibility(View.GONE);
	}
	
	public void addQuantity(){
		if((buyQuantity+1)*PUprice < SRGameManager.getInstace().getTotalPoints()){
			buyQuantity++;
			quantityLabel.setText(" "+buyQuantity+" ");
			priceLabel.setText(buyQuantity*PUprice+" "+res.getString(R.string.points));
		}
	}
	
	public void deductQuantity(){
		if(buyQuantity>1)
		buyQuantity--;
		quantityLabel.setText(" "+buyQuantity+" ");
		priceLabel.setText(buyQuantity*PUprice+" "+res.getString(R.string.points));
	}
	
	public void buyPowerUps(){
		//TODO guardar les coses comprades
	}
	
	Rect rect;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ViewGroup.MarginLayoutParams vlp = (MarginLayoutParams) getLayoutParams();
		float userX = event.getRawX();
		float userY = event.getRawY();
		float circleCenterX = vlp.leftMargin + OBJECT_WIDTH/2;
		float circleCenterY = vlp.topMargin + OBJECT_HEIGHT/2;
		float radi = OBJECT_WIDTH/2;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
			if(v == button && Math.sqrt(Math.pow(userX-circleCenterX, 2)+Math.pow(userY-circleCenterY, 2))< radi){				
				setBackgroundResource(R.drawable.pumenu_pressed);
				buyButton.setTextColor(Color.GRAY);
			}else if(v == oneLess){
				oneLess.setImageResource(R.drawable.deductpu_pressed);
			}else if(v == oneMore){
				oneMore.setImageResource(R.drawable.addpu_pressed);
			}
			break;
		case MotionEvent.ACTION_UP:
			Boolean isOn = true;
			if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                isOn = false;
            }
			if(v == button){
				if(Math.sqrt(Math.pow(userX-circleCenterX, 2)+Math.pow(userY-circleCenterY, 2))< radi){
					if(buyQuantity*PUprice < SRGameManager.getInstace().getTotalPoints()){
						mBuyListener.onEvent(PUtype, buyQuantity);
						buyPowerUps();
						//TODO animacio no tens prou diners
					}					
				}
				setBackgroundResource(R.drawable.pumenu);
				buyButton.setTextColor(Color.WHITE);
			}else if(v == oneLess){
				if(isOn){
					deductQuantity();
				}
				oneLess.setImageResource(R.drawable.deductpu);
			}else if(v == oneMore){
				if(isOn){
					addQuantity();
				}
				oneMore.setImageResource(R.drawable.addpu);
			}			
			break;
		default:
			break;
		}
		return true;
	}
}
