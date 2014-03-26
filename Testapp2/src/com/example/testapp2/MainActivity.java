package com.example.testapp2;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.HashMap;

import org.quickconnectfamily.json.*;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(19)
public class MainActivity extends Activity {

	ViewGroup container;
	Scene current;
	Scene other;
	Scene confirm;
	Scene register;
	String uNameString;
	Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		container = (ViewGroup) findViewById(R.id.container);
		current = Scene.getSceneForLayout(container, R.layout.scene01, this);
		confirm = Scene.getSceneForLayout(container, R.layout.logonconfirmation, this);
		register = Scene.getSceneForLayout(container, R.layout.registerpage, this);
		current.enter();
		handler = new Handler();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void transition(HashMap aMap){
		if (aMap.containsValue(true))
		TransitionManager.go(confirm);
		else
		TransitionManager.go(register);
	}
	
	public class PostRunnable implements Runnable {
		private HashMap aMap;
		private WeakReference<MainActivity> weak;
		
		public PostRunnable(HashMap aMap, WeakReference<MainActivity> weak){
			this.aMap = aMap;
			this.weak = weak;
		}
		@Override
		public void run() {
			MainActivity mainActivity = weak.get();
			mainActivity.transition(aMap);
		}
		
	}
	public class LoginRunnable implements Runnable {

		private Handler handler;
		private UserBean currentUser;
		private HashMap aMap;
		private WeakReference<MainActivity> weak;

		public LoginRunnable(Handler handler, UserBean currentUser, WeakReference<MainActivity> weak) {
			this.handler = handler;
			this.currentUser = currentUser;
			this.weak = weak;
		}
		@Override
		public void run() {
			try{
				//connect to the server
				Socket toServer = new Socket("10.0.2.2", 6000);
				System.out.println("socket created");
				// setup the JSON streams to be used later.
				JSONInputStream inFromServer = new JSONInputStream(toServer.getInputStream());
				JSONOutputStream outToServer = new JSONOutputStream(toServer.getOutputStream()); 
				System.out.println("JSON streams set up");
				outToServer.writeObject(currentUser);
				aMap = (HashMap)inFromServer.readObject();
				PostRunnable postRunnable = new PostRunnable(aMap, weak);
				handler.post(postRunnable); 
				toSceneMain();
			} catch(Exception e){
				e.printStackTrace();
				// responseView.setText("Error: Unable to communicate with server. "+e.getLocalizedMessage());
			}
		}

	}
	public void sendlogon(View view) {
		EditText uName = (EditText) findViewById(R.id.uNameText);
		uNameString = uName.getText().toString();
		EditText password = (EditText) findViewById(R.id.passwordText);
		String passwordString = password.getText().toString();
		UserBean currentUser = new UserBean();
		currentUser.setuName(uNameString);
		currentUser.setPassword(passwordString);
		WeakReference<MainActivity> weak = new WeakReference<MainActivity>(this);
		LoginRunnable run = new LoginRunnable(handler, currentUser, weak);
		Thread connectThread = new Thread(run);
		connectThread.start();
		
	}
	
	public void toSceneMain(){
		TransitionManager.go(confirm);
		TextView welcome = (TextView) findViewById(R.id.welcome);
		welcome.setText("Welcome " + uNameString);
	}

	public void startregister(View view){
		TransitionManager.go(register);
	}

	public void sendRegister(View view){
		EditText uName = (EditText) findViewById(R.id.regUNameText);
		String uNameString = uName.getText().toString();
		EditText password1 = (EditText) findViewById(R.id.password1Text);
		String password1String = password1.getText().toString();
		EditText password2 = (EditText) findViewById(R.id.password2text);
		String password2String = password2.getText().toString();
		if (password2String.equals(password1String)){
			UserBean currentUser = new UserBean();
			currentUser.setuName(uNameString);
			currentUser.setPassword(password1String);
			WeakReference<MainActivity> weak = new WeakReference<MainActivity>(this);
			Thread connectThread = new Thread(new LoginRunnable(handler, currentUser, weak));
			connectThread.start();
			TransitionManager.go(confirm);
			TextView welcome = (TextView) findViewById(R.id.welcome);
			welcome.setText("Welcome " + uNameString);
		}
		else{
			Context context = getApplicationContext();
			CharSequence text = "Passwords do not match!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();

		}
	}

}