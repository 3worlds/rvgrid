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
public class StateMachineController
		extends AbstractGridNode
		implements StateMachineObserver {

	private static Logger log = Logging.getLogger(StateMachineController.class);

	private StateMachineEngine<StateMachineController> stateMachine;

	public StateMachineController(StateMachineEngine<StateMachineController> observed) {
		super();
		stateMachine = observed;
		stateMachine.addObserver(this);
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
	@Override
	public void onStatusMessage(State newState) {
		log.info(()->"Oh! state machine now in state "+newState.getName());
	}

	/**
	 * sends an event to trigger a transition in the state machine
	 *
	 * @param event the event to send
	 */
	public void sendEvent(Event event) {
		log.info(()->"Sending event "+event.getName()+" to state machine");
		RVMessage eventMessage = new RVMessage(event.getMessageType(),null,this,stateMachine);
		stateMachine.callRendezvous(eventMessage);
	}

	public int statusMessageCode() {
		return stateMachine.STATUS_MESSAGE;
	}

	public StateMachineEngine<? extends GridNode> stateMachine() {
		return stateMachine;
	}

}
