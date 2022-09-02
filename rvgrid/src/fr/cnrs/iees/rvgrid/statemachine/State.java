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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.ens.biologie.generic.Sealable;

/**
 * A discrete state characteristic of a state machine.
 *  
 * @author Shayne Flint - 2012
 * 			refactored by J. Gignoux - Aug. 2019
 *
 */
public final class State implements Sealable {
	
	private boolean sealed = false;
	private String name;
	private Procedure procedure;
	private List<Transition> transitionList = new ArrayList<Transition>();

	/**
	 * <p>States must be created before transitions, then transitions are added through the
	 * {@linkplain State#addTransition addTransition(...)} method.</p>
	 * <p>When entering a state, an action may be required. It is represented here by the 
	 * {@linkplain Procedure} argument: every time this state is entered, the procedure will be
	 * executed.</p>
	 * 
	 * @param name the name of this state
	 * @param procedure the action performed when entering this state
	 */
	public State(String name, Procedure procedure) {
		super();
		this.name = name;
		this.procedure = procedure;
	}
	
	/**
	 * <p>States must be created before transitions, then transitions are added through the
	 * {@linkplain State#addTransition addTransition(...)} method.</p>
	 * 
	 * <p>Use this constructor when no action is taken on entering the state.</p>
	 * 
	 * @param name the name of this state
	 */
	public State(String name) {
		this(name,new Procedure());
	}
	
	// initialisation methods
	
	/**
	 * <p>Add a transition to another state.</p>
	 * <ol>
	 * <li>Transitions can only be added while this instance is <em>unsealed</em> (cf. the
	 * {@link Sealable} class). A call to the {@link State#seal seal()} method will
	 * secure this instance by preventing further addition of any other transition.</li>
	 * <li>All transitions must be triggered by a different {@link Event}. The 
	 * {@code seal()} method will check for this condition and throw an Exception if it
	 * is false.</li>
	 * </ol>
	 * 
	 * @param transition The {@link Transition} to add.
	 */
	public void addTransition(Transition transition) {
		if (!sealed)
			transitionList.add(transition);
		else
			throw new IllegalStateException("State: attempt to modify sealed data");
	}
	
	// other methods

	/**
	 * 
	 * @return the state name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return the procedure exectued when entering this state
	 */
	public Procedure getProcedure() {
		return procedure;
	}
	
	/**
	 * 
	 * @return the list of all transitions from this state to other states
	 */
	public List<Transition> getTransitions() {
		return transitionList;
	}
	
	/**
	 * A state is <em>quiescent</em> when it has no transitions to any other state, i.e. when it
	 * is a final state: there is no way to leave it.
	 * 
	 * @return true if this state is quiescent
	 */
	public boolean isQuiescent() {
		return transitionList.size() == 0;
	}

	// Object
	
	@Override
	public String toString() {
		return "[State " + name + " with " + procedure + "]";
	}
	
	// Sealable

	@Override
	public Sealable seal() {
		// check all outgoing transitions have a different event
		Set<Event> events = new HashSet<Event>();
		for (Transition t:transitionList)
			if (!events.add(t.getEvent()))
				throw new IllegalStateException("Error in state machine design: state '"+name
					+"' has two outgoing transition triggered by the same event '"
					+t.getEvent().getName()+"'");
		sealed = true;
		return this;
	}

	@Override
	public boolean isSealed() {
		return sealed;
	}
	
}

