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
 *	A transition between two states
 * 
 * @author Shayne Flint - 2012
 *
 */
public class Transition {

	private State toState;
	private Event event;
	private Procedure procedure;
	private Guard guard;
	
	// Constructors
	// we are assuming here that transitions must be constructed after their states.
	
	public Transition(State toState, Event event, Procedure procedure, Guard guard) {
		super();
		this.toState = toState;
		this.event =  event;
		this.procedure = procedure;
		this.guard = guard;
	}

	public Transition(State toState, Event event, Procedure procedure) {
		this(toState,event,procedure,new Guard());
	}

	public Transition(State toState, Event event, Guard guard) {
		this(toState,event,new Procedure(),guard);
	}

	public Transition(State toState, Event event) {
		this(toState,event,new Procedure(),new Guard());
	}
	
	// accessors

	public State getToState() {
		return toState;
	}

	public Event getEvent() {
		return event;
	}

	public Procedure getProcedure() {
		return procedure;
	}
	
	public Guard getGuard() {
		return guard;
	}

	// Object

	@Override
	public String toString() {
		return "[Transition to " + toState + " in response to " + event + " calling " + procedure + "]";
	}

}
