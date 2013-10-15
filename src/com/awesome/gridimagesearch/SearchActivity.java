package com.awesome.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	
	private EditText etQuery;
	private GridView gvResults;
	private Button btnSearch;
	ArrayList<ImageResult> imageResults;
	ImageResultArrayAdapter imageAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        imageAdapter = new ImageResultArrayAdapter(this, imageResults);
        gvResults.setAdapter(imageAdapter);
        gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long rowId) {
				Intent i = new Intent(getApplicationContext(),ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }
    
    public void setupViews() {
    	etQuery = (EditText) findViewById(R.id.etQuery);
    	gvResults = (GridView) findViewById(R.id.gvResults);
    	btnSearch = (Button) findViewById(R.id.btnSearch);
    	imageResults = new ArrayList<ImageResult>();
    }
    
    public void onImageSearch(View v){
    	String query = etQuery.getText().toString();
    	Toast.makeText(this, "Searching for "+query, Toast.LENGTH_LONG).show();
    	AsyncHttpClient client = new AsyncHttpClient();
    	//https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=jehaan
    	client.get("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q="+Uri.encode(query)+"&start="+0, 
    			new JsonHttpResponseHandler(){
    				public void onSuccess(JSONObject response){
    					JSONArray imageJsonResults = null;
    					try{
    						imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
    						imageResults.clear();
    						imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
    						Log.d("DEBUG",imageResults.toString());
    					}
    					catch(JSONException e){
    						e.printStackTrace();
    					}
    				}
    	});
    	
    }
    
}
