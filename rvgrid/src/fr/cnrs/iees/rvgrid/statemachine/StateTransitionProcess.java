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

import fr.cnrs.iees.rvgrid.rendezvous.RVMessage;
import fr.cnrs.iees.rvgrid.rendezvous.RendezvousProcess;

/**
 * Executes what should be executed at a state machine transition between states.
 * 
 * @author Jacques Gignoux - 14 août 2019
 *
 */
public class StateTransitionProcess implements RendezvousProcess {
	
	// temporary
	public static final int STATE_MACHINE_RESET = 125478;
	public static final int STATE_MACHINE_STRUCTURE = STATE_MACHINE_RESET+1;
	public static final int STATE_MACHINE_CHANGED_STATE = STATE_MACHINE_STRUCTURE+1;

	public StateTransitionProcess() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(RVMessage message) {
		StateMachineNode<?> stateMachine = (StateMachineNode<?>) message.getMessageHeader().target();
		switch (message.getMessageHeader().type()) {
		case STATE_MACHINE_RESET:
			 stateMachine.setCurrentState(null);
			 stateMachine.reset();
			 stateMachine.sendMessage(STATE_MACHINE_CHANGED_STATE,stateMachine.getCurrentState());
			// notifyStateChanged()
			break;
		case STATE_MACHINE_STRUCTURE:
			if (stateMachine.getCurrentState() == null) 
//				log.info("   - initial pseudo state");
				for (Transition t : stateMachine.getInitialPseudoStates()) {
					if (t.getEvent().getMessageType() == message.getMessageHeader().type()) {
						boolean proceed = t.getGuard().proceed(stateMachine, message);
						if (proceed) {
							t.getProcedure().run(stateMachine, message);
//							log.info("   - initialising transition procedure called");
							t.getToState().getProcedure().run(stateMachine, message);
//							log.info("   - initial state procedure called");
							stateMachine.setCurrentState(t.getToState());
//							log.info("   - now in state " + t.getToState());
//							//					notifyInitialised();
							 stateMachine.sendMessage(STATE_MACHINE_CHANGED_STATE,stateMachine.getCurrentState());
//							if (stateMachine.getCurrentState().getTransitions().size() > 0)
//								log.info("   - initial state '" + stateMachine.getCurrentState() + "'");
//							else {
//								log.info("   - initial (quiescent) state '" + stateMachine.getCurrentState() + "'");
//								//						notifyQuiescentState();
//							}
//							processQueuedMessages();
//							return this;	
						}
					}
				}
			break;
		case STATE_MACHINE_CHANGED_STATE:
//			log.info("   - current state is '" + stateMachine.getCurrentState() + "'");
			for (Transition transition : stateMachine.getCurrentState().getTransitions() ) {
				if (transition.getEvent().getMessageType() == message.getMessageHeader().type()) {
					boolean enabled = transition.getGuard().proceed(stateMachine, message);
					if (enabled) {
						transition.getProcedure().run(stateMachine, message);
//						log.info("   - transition procedure called");
						transition.getToState().getProcedure().run(stateMachine, message);
//						log.info("   - new state procedure called");
						stateMachine.setCurrentState(transition.getToState());
						 stateMachine.sendMessage(STATE_MACHINE_CHANGED_STATE,stateMachine.getCurrentState());
//						if (stateMachine.getCurrentState().getTransitions().size() > 0)
//							log.info("   - now in state '" + stateMachine.getCurrentState() + "'");
//						else {
//							log.info("   - now in (quiescent) state '" + stateMachine.getCurrentState() + "'");
//							//						notifyQuiescentState();
//						}
//						processQueuedMessages();
//						return this;
					}
				}
			}
			break;
			default:
				// log: unknown message type ?
				;
		}
	}

}
