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

import fr.cnrs.iees.rvgrid.rendezvous.AbstractGridNode;
import fr.cnrs.iees.rvgrid.rendezvous.RVMessage;
import fr.cnrs.iees.rvgrid.rendezvous.RendezvousProcess;

/**
 * The {@link RendezvousProcess} class to use with {@link StateMachineEngine}.
 * Executes what should be executed at a state machine transition between states. This code is 
 * invisibly called by {@link AbstractGridNode#callRendezvous callRendezvous(...)} and should
 * never be directly manipulated. 
 * 
 * @author Jacques Gignoux - 14 août 2019
 *
 */
class StateTransitionProcess implements RendezvousProcess {

	private void transition(Iterable<Transition> transitionList,
			StateMachineEngine<?> stateMachine, 
			RVMessage message) {
		for (Transition transition : transitionList ) {
			if (transition.getEvent().getMessageType() == message.getMessageHeader().type()) {
				boolean enabled = transition.getGuard().proceed(stateMachine, message);
				if (enabled) {
					transition.getProcedure().run(stateMachine, message);
					transition.getToState().getProcedure().run(stateMachine, message);
					stateMachine.setCurrentState(transition.getToState());
				}
			}
		}
	}
	
	// this is executed when a state machine receives a message containing an event triggering
	// a state change
	@Override
	public void execute(RVMessage message) {
		StateMachineEngine<?> stateMachine = (StateMachineEngine<?>) message.getMessageHeader().target();
		if (stateMachine.getCurrentState() == null)
			transition(stateMachine.getInitialPseudoStates(),stateMachine,message);
		else
			transition(stateMachine.getCurrentState().getTransitions(),stateMachine,message);
	}

}
