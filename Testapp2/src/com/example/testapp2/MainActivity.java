package com.example.testapp2;

import java.net.Socket;

import org.quickconnectfamily.json.*;

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
	Socket toServer;
	JSONInputStream inFromServer;
	JSONOutputStream outToServer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		container = (ViewGroup) findViewById(R.id.container);
		current = Scene.getSceneForLayout(container, R.layout.scene01, this);
		other = Scene.getSceneForLayout(container, R.layout.logonconfirmation, this);
		current.enter();
		try{
        	//connect to the server
	        toServer = new Socket("172.0.0.1", 6000);
	        //setup the JSON streams to be used later.
	        inFromServer = new JSONInputStream(toServer.getInputStream());
	        outToServer = new JSONOutputStream(toServer.getOutputStream());
		} catch(Exception e){
            e.printStackTrace();
            // responseView.setText("Error: Unable to communicate with server. "+e.getLocalizedMessage());
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void sendlogon(View view) {
		EditText uName = (EditText) findViewById(R.id.uNameText);
		String uNameString = uName.getText().toString();
		EditText password = (EditText) findViewById(R.id.passwordText);
		String passwordString = password.getText().toString();
		UserBean currentUser = new UserBean();
		currentUser.setuName(uNameString);
		currentUser.setPassword(passwordString);
		TransitionManager.go(other);
		TextView welcome = (TextView) findViewById(R.id.welcome);
		welcome.setText("Welcome " + uNameString);
	}

}