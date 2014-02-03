package com.example.testapp2;

import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

@TargetApi(19)
public class MainActivity extends Activity {

	ViewGroup container;
	Scene current;
	Scene other;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		container = (ViewGroup) findViewById(R.id.container);
		current = Scene.getSceneForLayout(container, R.layout.scene01, this);
		other = Scene.getSceneForLayout(container, R.layout.logonconfirmation, this);
		current.enter();
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
		TransitionManager.go(other);
		TextView welcome = (TextView) findViewById(R.id.welcome);
		welcome.setText("Welcome " + message);
	}

}
