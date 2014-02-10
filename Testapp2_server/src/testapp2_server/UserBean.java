package testapp2_server;

import java.io.Serializable;


public class UserBean implements Serializable{

	private String uName;
	private String password;

	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
