package com.couchbase.grocerysync;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.DbInfo;
import org.ektorp.ViewQuery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CouchListAdapter extends BaseAdapter {
	
	protected Context context;
	public HashMap<String, GroceryItem> rowMap;
	
	public CouchListAdapter(Context context, CouchDbConnector couchDbConnector) {
		this.context = context;
		
		rowMap = new HashMap<String, GroceryItem>();
		
		DbInfo dbInfo = couchDbConnector.getDbInfo();
		long lastUpdateSeq = dbInfo.getUpdateSeq();
		
		List<GroceryItem> items = couchDbConnector.queryView(new ViewQuery().allDocs().includeDocs(true), GroceryItem.class);
		Iterator<GroceryItem> itemIterator = items.iterator();
		while(itemIterator.hasNext()) {
			GroceryItem item = itemIterator.next();
			rowMap.put(item.getId(), item);
		}
		
		//create an ansyc task to get updates
		CouchChangesAsyncTask couchChangesAsyncTask = new CouchChangesAsyncTask(this, couchDbConnector, lastUpdateSeq);
		couchChangesAsyncTask.execute((Integer[])null);
	}

	@Override
	public int getCount() {
		return rowMap.size();
	}

	@Override
	public Object getItem(int position) {
		String key = (String)rowMap.keySet().toArray()[position];
		return rowMap.get(key);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View itemView, ViewGroup parent) {
        View v = itemView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.grocery_list_item, null);
        }
        
        TextView label = (TextView) v.findViewById(R.id.label);
        GroceryItem item  = (GroceryItem)getItem(position);
        if(item.getText() != null) {
        	label.setText(item.getText());
        }
        else {
        	label.setText("");
        }
        
        if(item.getCheck() != null) {
	        ImageView icon = (ImageView) v.findViewById(R.id.icon);
	        if(item.getCheck().booleanValue()) {
	        	icon.setImageResource(R.drawable.list_area___checkbox___checked);
	        }
	        else {
	        	icon.setImageResource(R.drawable.list_area___checkbox___unchecked);
	        }
        }
        
        
        return v;
	}

}
