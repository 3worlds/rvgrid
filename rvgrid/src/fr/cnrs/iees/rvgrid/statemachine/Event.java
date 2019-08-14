package fr.cnrs.iees.rvgrid.statemachine;

/**
 * An event triggering a state transition
 * 
 * @author Shayne Flint - 2012
 *
 */
public class Event {

	private int messageType;
	private String name;
	private boolean isInternal = false;
	
	public Event() {
		messageType = -1;
		name = null;
	}
	
	public Event(int messageType, String name) {
		this.messageType = messageType;
		this.name = name;
	}
	
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	
	public int getMessageType() {
		return messageType;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isInternal() {
		return isInternal;	
	}
	
	public boolean isExternal() {
		return !isInternal;	
	}
		
	public void setInternal(boolean state) {
		isInternal = state;
	}
	
	public void setInternal() {
		setInternal(true);
	}
	
	public void setExternal() {
		setInternal(false);
	}
	
	public String toString() {
		return "[Event name='" + name + "', messageType=" + messageType + "]";
	}
}
