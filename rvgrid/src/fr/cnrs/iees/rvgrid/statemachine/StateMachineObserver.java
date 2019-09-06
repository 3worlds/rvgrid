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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import fr.cnrs.iees.rvgrid.rendezvous.AbstractGridNode;
import fr.cnrs.iees.rvgrid.rendezvous.GridNode;
import fr.cnrs.iees.rvgrid.rendezvous.RVMessage;
import fr.cnrs.iees.rvgrid.rendezvous.RendezvousProcess;
import fr.ens.biologie.generic.utils.Logging;

/**
 * A class able to understand a state machine and send it relevant messages
 * 
 * @author Jacques Gignoux - 16 août 2019
 *
 */
public class StateMachineObserver extends AbstractGridNode {

	private static Logger log = Logging.getLogger(StateMachineObserver.class);
	
	private StateMachineEngine<? extends GridNode> stateMachine;
	
	/** JG-added 30/9/2015 - a convenience to quickly get the event codes from their names 	 */
	private Map<String, Integer> eventCodes = new HashMap<String, Integer>();
	
	
	private void recordEvents() {
		Set<Event> events = new HashSet<Event>();
		for (State s:stateMachine.getStates())
			for (Transition t:s.getTransitions())
				events.add(t.getEvent());
		for (Transition t:stateMachine.getInitialPseudoStates())
			events.add(t.getEvent());
		for (Event e:events)
			eventCodes.put(e.getName(),e.getMessageType());
	}
	
	public StateMachineObserver(StateMachineEngine<? extends GridNode> observed) {
		super();
		stateMachine = observed;
		recordEvents();
		addRendezvous(new RendezvousProcess() {
			@Override
			public void execute(RVMessage message) {
				if (message.getMessageHeader().type()==stateMachine.STATUS_MESSAGE) {
					State state = (State) message.payload();
					onStatusMessage(state);
				}
			}
		},stateMachine.STATUS_MESSAGE);
	}
	
	/**
	 * computes what happens when a state machine returns its state.
	 * Meant to be overriden by descendants.
	 * 
	 * @param newState the new state in which the state machine arrived
	 */
	public void onStatusMessage(State newState) {
		log.info("Oh! state machine now in state "+newState.getName());
	}
	
	/**
	 * sends an event to trigger a transition in the state machine
	 * 
	 * @param event the event to send
	 */
	public void sendEvent(Event event) {
		log.info("Sending event "+event.getName()+" to state machine");
		RVMessage eventMessage = new RVMessage(event.getMessageType(),null,this,stateMachine);
		stateMachine.callRendezvous(eventMessage);
	}

}
