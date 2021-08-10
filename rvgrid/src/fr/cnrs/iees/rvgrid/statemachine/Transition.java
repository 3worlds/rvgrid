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
 * <p>A transition between two states.</p>
 *
 * <p>Since we assume that transitions are constructed after {@link State}s, the state of origin is
 * the one on which {@link State#addTransition addTransition(...)} is called, e.g.:
 * </p> 
 * <pre>
 * waiting = new State("waiting");
 * running = new State("running");
 * run = new Event(1,"run");
 * waiting.addTransition(new Transition(running,run));
 * </pre>
 * <p>In other words, only the target state must be provided to the {@code Transition} constructor.</p>
 * 
 * @author Shayne Flint - 2012
 *
 */
public class Transition {

	private State toState;
	private Event event;
	private Procedure procedure;
	private Guard guard;
	
	/**
	 * Generic constructor. 
	 * 
	 * @param toState the target state
	 * @param event the event triggering this transition
	 * @param procedure the procedure to execute when the transition occurs
	 * @param guard the guard condition to check before executing procedure
	 */
	public Transition(State toState, Event event, Procedure procedure, Guard guard) {
		super();
		this.toState = toState;
		this.event =  event;
		this.procedure = procedure;
		this.guard = guard;
	}

	/**
	 * Constructor with no guard.
	 * 
	 * @param toState the target state
	 * @param event the event triggering this transition
	 * @param procedure the procedure to execute when the transition occurs
	 */
	public Transition(State toState, Event event, Procedure procedure) {
		this(toState,event,procedure,new Guard());
	}

	/**
	 * Constructor with no procedure. 
	 * 
	 * @param toState the target state
	 * @param event the event triggering this transition
	 * @param guard the guard condition to check before executing procedure
	 */
	public Transition(State toState, Event event, Guard guard) {
		this(toState,event,new Procedure(),guard);
	}

	/**
	 * Constructor with neither guard nor procedure. Will suffice in most usual cases.
	 * 
	 * @param toState the target state
	 * @param event the event triggering this transition
	 */
	public Transition(State toState, Event event) {
		this(toState,event,new Procedure(),new Guard());
	}
	
	// accessors

	/**
	 * 
	 * @return the target state
	 */
	public State getToState() {
		return toState;
	}

	/**
	 * 
	 * @return the event triggering this transition
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * 
	 * @return the procedure executed when this transition occurs
	 */
	public Procedure getProcedure() {
		return procedure;
	}
	
	/**
	 * 
	 * @return the guard condition to check before executing procedure
	 */
	public Guard getGuard() {
		return guard;
	}

	// Object

	@Override
	public String toString() {
		return "[Transition to " + toState + " in response to " + event + " calling " + procedure + "]";
	}

}
