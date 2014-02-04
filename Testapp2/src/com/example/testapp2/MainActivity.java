package com.example.testapp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.TargetApi;
import android.app.Activity;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
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
	Transition mytransition;
	private ServerSocket serverSocket;

	Handler updateConversationHandler;

	Thread serverThread = null;

	private TextView text;

	public static final int SERVERPORT = 6000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		container = (ViewGroup) findViewById(R.id.container);
		current = Scene.getSceneForLayout(container, R.layout.scene01, this);
		other = Scene.getSceneForLayout(container, R.layout.logonconfirmation, this);
		current.enter();
		mytransition = TransitionInflater.from(this)
			    .inflateTransition(R.transition.transition);
	
				text = (TextView) findViewById(R.id.textView1);

				updateConversationHandler = new Handler();

			//the server side will include the ServerThread class which we can call
				
				this.serverThread = new Thread(new ServerThread());
				this.serverThread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	protected void onStop() {
		super.onStop();
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendlogon(View view) {
		EditText editText = (EditText) findViewById(R.id.uNameText);
		String message = editText.getText().toString();
		TransitionManager.go(other,mytransition);
		TextView welcome = (TextView) findViewById(R.id.welcome);
		welcome.setText("Welcome " + message);
	}

	class ServerThread implements Runnable {

		public void run() {
			Socket socket = null;
			try {
				serverSocket = new ServerSocket(SERVERPORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (!Thread.currentThread().isInterrupted()) {

				try {

					socket = serverSocket.accept();

					CommunicationThread commThread = new CommunicationThread(socket);
					new Thread(commThread).start();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class CommunicationThread implements Runnable {

		private Socket clientSocket;

		private BufferedReader input;

		public CommunicationThread(Socket clientSocket) {

			this.clientSocket = clientSocket;

			try {

				this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {

			while (!Thread.currentThread().isInterrupted()) {

				try {

					String read = input.readLine();

					updateConversationHandler.post(new updateUIThread(read));

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	class updateUIThread implements Runnable {
		private String msg;

		public updateUIThread(String str) {
			this.msg = str;
		}

		@Override
		public void run() {
			text.setText(text.getText().toString()+"Client Says: "+ msg + "\n");
		}
	}
}

