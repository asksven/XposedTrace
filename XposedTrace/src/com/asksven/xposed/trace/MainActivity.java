package com.asksven.xposed.trace;

import com.asksven.xposed.trace.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    public boolean onOptionsItemSelected(MenuItem item)
    {  
        switch (item.getItemId())
        {
	        case R.id.action_settings:  
	        	Intent intentPrefs = new Intent(this, SettingsActivity.class);
	            this.startActivity(intentPrefs);
	        	break;	
        }
        
        return true;
    }


}
