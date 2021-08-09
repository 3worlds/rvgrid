/**************************************************************************
 *  RVGRID - A light-weight rendezvous system                             *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          * 
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  RVGRID is a A light-weight implementation of ADA's rendez-vous        *
 *  messaging pattern                                                     *
 *                                                                        *   
 **************************************************************************
 *  This file is part of RVGRID.                                          *
 *                                                                        *
 *  RVGRID is free software: you can redistribute it and/or modify        *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  RVGRID is distributed in the hope that it will be useful,             *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with RVGRID.                                                    *
 *  If not, see <https://www.gnu.org/licenses/gpl.html>                   *
 *                                                                        *
 **************************************************************************/
package fr.cnrs.iees.rvgrid.statemachine;

/**
 * An event triggering a state transition.
 * 
 * @author Shayne Flint - 2012
 *
 */
public class Event {

	private int messageType;
	private String name;
	private boolean isInternal = false;
	
	/**
	 * Use this constructor to instantiate <em>internal</em> events, i.e. events that are
	 * created by the state machine they belong to. E.g., a 'check conditions' event may automatically
	 * follow a 'save' or 'update' user action; 'check conditions' is internal as it doesnt depend
	 * on any external intervention.
	 *   
	 * @param messageType the message type associated to this event. <strong>NB: all events in a state machine
	 * must have a different messageType</strong>
	 * @param name the name of this event, e.g. 'piss off' or 'blink'
	 * @param internal true if the event is internal
	 */
	public Event(int messageType, String name, boolean internal) {
		super();
		this.messageType = messageType;
		this.name = name;
		this.isInternal = internal;
	}

	/**
	 * Use this constructor for <em>external</em> events, i.e. events that are created from
	 * outside the state machine they belong to (e.g. events triggered by a mouse click of the user)
	 * 
	 * @param messageType the message type associated to this event. <strong>NB: all events in a state machine
	 * must have a different messageType</strong>
	 * @param name the name of this event, e.g. 'start' or 'blow up'
	 */
	public Event(int messageType, String name) {
		this(messageType,name,false);
	}
	
	/**
	 * 
	 * @return the message type associated to this event
	 */
	public int getMessageType() {
		return messageType;
	}
	
	/**
	 * 
	 * @return the event name
	 */
	public String getName() {
		return name;
	}

	/**
	 * An event is <em>internal</em> when it is created by a {@link Procedure} contained in
	 * the state machine it is recorded in.
	 *  
	 * @return true if this event is internal
	 */
	public boolean isInternal() {
		return isInternal;	
	}
	
	/**
	 * An event is <em>external</em> when it is created from outside the state machine it is 
	 * recorded in (e.g. by a user clicking on a button).
	 *  
	 * @return true if the event is external
	 */
	public boolean isExternal() {
		return !isInternal;	
	}
		
	@Override
	public String toString() {
		return "[Event name='" + name + "', messageType=" + messageType + "]";
	}
}
