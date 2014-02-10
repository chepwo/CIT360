package testapp2_server;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class CommunicationBean implements Serializable {

	private String message;
	private ArrayList object;

	public CommunicationBean() {

	}
	
	public CommunicationBean(String message,ArrayList object) {
		this.message = message;
		this.object = object;
	}
	
	public ArrayList getObject() {
		return object;
	}

	public void setObject(ArrayList object) {
		this.object = object;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommunicationBean other = (CommunicationBean) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		return true;
	}
	
}
