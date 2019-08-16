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
	
	public Event(int messageType, String name, boolean internal) {
		super();
		this.messageType = messageType;
		this.name = name;
		this.isInternal = internal;
	}
	
	public Event(int messageType, String name) {
		this(messageType,name,false);
	}
	
	public int getMessageType() {
		return messageType;
	}
	
	public String getName() {
		return name;
	}

	/**
	 * An event is internal when it is triggered by a Procedure of one of the states
	 * @return
	 */
	public boolean isInternal() {
		return isInternal;	
	}
	
	/**
	 * An event is external when it is triggered by user (or some other external) intervention 
	 * @return
	 */
	public boolean isExternal() {
		return !isInternal;	
	}
		
	public String toString() {
		return "[Event name='" + name + "', messageType=" + messageType + "]";
	}
}
