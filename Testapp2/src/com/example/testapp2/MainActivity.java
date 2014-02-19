package com.example.testapp2;

import java.net.Socket;

import org.quickconnectfamily.json.*;

import android.os.Bundle;
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
	Socket toServer;
	JSONInputStream inFromServer;
	JSONOutputStream outToServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println(" --------  debug 1 ------------");
		setContentView(R.layout.activity_main);
		container = (ViewGroup) findViewById(R.id.container);
		current = Scene.getSceneForLayout(container, R.layout.scene01, this);
		confirm = Scene.getSceneForLayout(container, R.layout.logonconfirmation, this);
		register = Scene.getSceneForLayout(container, R.layout.registerpage, this);
		current.enter();
		System.out.println(" --------  debug 2 ------------");
		Thread connectThread = new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					//connect to the server
					toServer = new Socket("10.0.2.2", 6000);
					System.out.println("socket created");
					// setup the JSON streams to be used later.
					inFromServer = new JSONInputStream(toServer.getInputStream());
					outToServer = new JSONOutputStream(toServer.getOutputStream()); 
					System.out.println("JSON streams set up");
				} catch(Exception e){
					e.printStackTrace();
					// responseView.setText("Error: Unable to communicate with server. "+e.getLocalizedMessage());
				}				
			}
		} );
		System.out.println(" --------  debug 3 ------------");
		connectThread.start();
		System.out.println(" --------  debug 4 ------------");


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
		System.out.println(" --------  debug 5 ------------");
		try {
			outToServer.writeObject(currentUser);
		}  
		catch (JSONException e) {
			e.printStackTrace();
			System.out.println(" --------- unable to send ------------");
		}
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
			try { 
				outToServer.writeObject(currentUser);
			} 
			catch (JSONException e) {
				e.printStackTrace();
				System.out.println(" --------- unable to send ------------");
			}
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