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
