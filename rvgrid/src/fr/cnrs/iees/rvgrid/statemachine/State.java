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

import fr.cnrs.iees.rvgrid.RvgridException;
import fr.ens.biologie.generic.Sealable;

/**
 * A discrete state characteristic of a state machine
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

	// Constructors
	// NB states must be created before transitions, then transitions are added through the
	// addTransition method
	
	public State(String name, Procedure procedure) {
		super();
		this.name = name;
		this.procedure = procedure;
	}
	
	public State(String name) {
		this(name,new Procedure());
	}
	
	// initialisation methods
	
	public void addTransition(Transition transition) {
		if (!sealed)
			transitionList.add(transition);
		else
			throw new RvgridException("State: attempt to modify sealed data");
	}
	
	// other methods

	public String getName() {
		return name;
	}
	
	public Procedure getProcedure() {
		return procedure;
	}
	
	public List<Transition> getTransitions() {
		return transitionList;
	}
	
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
				throw new RvgridException("Error in state machine design: state '"+name
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

