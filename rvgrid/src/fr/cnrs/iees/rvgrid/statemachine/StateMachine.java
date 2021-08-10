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

import java.util.Collection;

/**
 * A finite state-machine. A {@link StateMachine} is defined by a list of {@link State}s, 
 * with {@link Transition}s between them triggered by {@link Event}s. Actions occurring
 * during a transition or when in a particular state are implemented using {@link Procedure}s.
 * 
 * @author Jacques Gignoux - 14 août 2019
 *
 */
public interface StateMachine {

	/**
	 * The initial pseudo-state (i.e. before the start of the state machine)
	 */
	public static final String INITIAL_PSEUDO_STATE = "<initial pseudo state>";
	
	/**
	 * <p>Read-only access to the states of this machine.</p>
	 * <p><strong>NB</strong>: Implementations must use {@code Collections.unmodifiableCollection(...)}
	 * to prevent accidental modification of the state list after machine initialisation.</p>
	 * 
	 * @return the immutable list of states
	 */
	public Collection<State> getStates();
	
	/**
	 * <p>Read-only access to the initial pseudo-states of this machine. Notice that a pseudo-state is 
	 * actually a transition where the initial state is unspecified.</p>
	 * <p><strong>NB</strong>: Implementations must use {@code Collections.unmodifiableCollection(...)}
	 * to prevent accidental modification of the initial pseudo-states list after machine initialisation.</p>
	 * 
	 * @return the immutable list of initial pseudo-states
	 */
	public Collection<Transition> getInitialPseudoStates(); // yes, this is correct
		
	/**
	 * <p>Read-only access to the transitions of this machine.</p>
	 * <p><strong>NB</strong>: Implementations must use {@code Collections.unmodifiableCollection(...)}
	 * to prevent accidental modification of the transition list after machine initialisation.</p>
	 * 
	 * @return the immutable list of transitions
	 */
	public Collection<Transition> getTransitions();
	
	/**
	 * Sets the current state of the state machine to its argument.
	 * 
	 * @param state the new current state
	 */
	public void setCurrentState(State state);
	
	/**
	 * 
	 * @return the current state of the machine
	 */
	public State getCurrentState();
	
	
	/**
	 * <p>Read-only access to the events of this machine.</p>
	 * <p><strong>NB</strong>: Implementations must use {@code Collections.unmodifiableCollection(...)}
	 * to prevent accidental modification of the event list after machine initialisation.</p>
	 * 
	 * @return the immutable list of events
	 */
	public Collection<Event> getEvents();
	
	/**
	 * Checks if the state machine has started.
	 * 
	 * @return {@code true} if the state machine is in its initial pseudo-state.
	 */
	public boolean inInitialState();
	
}
