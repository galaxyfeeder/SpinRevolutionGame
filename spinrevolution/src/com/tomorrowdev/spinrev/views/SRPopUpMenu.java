package com.tomorrowdev.spinrev.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tomorrowdev.spinrev.R;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class SRPopUpMenu extends Dialog{
	
	public ListView listview;
	Context ctx;
	Resources res;

	public SRPopUpMenu(Context context) {
		super(context);		
		ctx = context;
		res = ctx.getResources();
		
		setTitle(res.getString(R.string.games_title));
		
		listview = new ListView(context);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		listview.setLayoutParams(params);
		
		 // Defined Array values to show in ListView
        String[] values = new String[] { res.getString(R.string.games_achievements), 
        								 res.getString(R.string.games_leaderboards)};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
          list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(context, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);       
		setContentView(listview);
	}
	
	private class StableArrayAdapter extends ArrayAdapter<String> {

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }
	  }
}