package testapp2_server;

import java.io.Serializable;

public class CommunicationBean implements Serializable {

	private int field1;
	private int field2;
	private String field3;
	private String field4;

	public CommunicationBean() {

	}

	public void setField1(int field1) {
		this.field1 = field1;
	}

	public void setField2(int field2) {
		this.field2 = field2;
	}
	
	public void setField3(String field3) {
		this.field3 = field3;
	}
	
	public void setField4(String field4) {
		this.field4 = field4;
	}
	
	public int getField1() {
		return this.field1;
	}
	
	public int getField2() {
		return this.field2;
	}
	
	public String getField3() {
		return this.field3;
	}
	
	public String getField4() {
		return this.field4;
	}
	
}
