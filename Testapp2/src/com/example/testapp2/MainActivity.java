package com.example.testapp2;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "This is the messege";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void sendlogon(View view) {
    	EditText editText = (EditText) findViewById(R.id.uNameText);
    	String message = editText.getText().toString();
	    setContentView(R.layout.logonconfirmation);
	    TextView welcome = (TextView) findViewById(R.id.welcome);
	    welcome.setText("Welcome " + message);
	}

}