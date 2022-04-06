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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.cnrs.iees.rvgrid.RvgridException;
import fr.cnrs.iees.rvgrid.observer.Observable;
import fr.cnrs.iees.rvgrid.rendezvous.GridNode;
import fr.cnrs.iees.rvgrid.rendezvous.AbstractGridNode;
import fr.cnrs.iees.rvgrid.rendezvous.RVMessage;
import fr.ens.biologie.generic.Resettable;
import fr.ens.biologie.generic.utils.Logging;

/**
 * <p>Implementation of a universal finite-state machine.</p>
 * <p>It is based on the ADA rendezvous pattern and on the Observer pattern. Instances of this
 * class are meant to respond to messages they receive at rendezvous by changing state, depending
 * on the events carried by messages.</p>  
 * 
 * <p>Here is an example of setting up a state machine for a simulator, i.e. a software that
 * can run, pause, step computations based on user actions (button clicks). This state diagram:</p>
 * 
 * <img src="{@docRoot}/../doc/images/simulator-state-machine.svg" align="middle" width="750" 
 * alt="a state transition diagram example"/>  
 * 
 * <p>is implemented as follows:</p>
 * <pre>
// states of the simulator
	waiting = new State("waiting");
	stepping = new State("stepping");
	pausing = new State("pausing");
	running = new State("running");
	quitting = new State("quitting");
	finished = new State("finished");
// events triggering transitions between states
	run = new Event(1,"run");
	step = new Event(2,"step");
	reset = new Event(3,"reset");
	goOn = new Event(4,"continue");
	pause = new Event(5,"pause");
	finalise = new Event(6,"finalise",true);
	quit = new Event(7,"quit");
	initialise = new Event(8,"initialise",true);
// transitions between states
	ips = new Transition(waiting,initialise);
	waiting.addTransition(new Transition(running,run));
	waiting.addTransition(new Transition(stepping,step));
	waiting.addTransition(new Transition(quitting,quit));
	running.addTransition(new Transition(pausing,pause));
	running.addTransition(new Transition(finished,finalise));
	stepping.addTransition(new Transition(running,goOn));
	stepping.addTransition(new Transition(waiting,reset));
	stepping.addTransition(new Transition(finished,finalise));
	stepping.addTransition(new Transition(quitting,quit));
	stepping.addTransition(new Transition(stepping,step));
	pausing.addTransition(new Transition(running,goOn));
	pausing.addTransition(new Transition(stepping,step));
	pausing.addTransition(new Transition(waiting,reset));
	pausing.addTransition(new Transition(quitting,quit));
	finished.addTransition(new Transition(quitting,quit));
	finished.addTransition(new Transition(waiting,reset));
// instanciation of state machine
	StateMachine sm = new StateMachineEngine<GridNode>(ips, waiting,stepping,pausing,running,quitting,finished);
</pre>
 * 
 * @author Shayne Flint - 2012<br/>
 * 			refactored by J. Gignoux  - Aug. 2019.
 *
 * @param <O> The {@link fr.cnrs.iees.rvgrid.observer.Observer Observer} class watching this state machine
 */
public class StateMachineEngine<O extends GridNode>
		extends AbstractGridNode
		implements Resettable, StateMachine, Observable<O> {

	private static Logger log = Logging.getLogger(StateMachineEngine.class);
	static {
		log.setLevel(Level.OFF);
	}

	/** The message code used to send information about states to this class instance
	 * observers. It is set in the constructor to max(event types)+1 */
	protected int STATUS_MESSAGE = 0;

	private Set<O> stateMachineListenerNodeList = new HashSet<O>();
	private List<State> stateList = new ArrayList<State>();
	private List<Transition> initialPseudoStateList = new ArrayList<Transition>();
	/** The current state */
	protected State currentState = null;

	// Constructors and initialisation methods

	// checks that all events have a different messageType index
	private Set<Event> checkEvents() {
		Set<Event> events = new HashSet<Event>();
		for (State s:stateList)
			for (Transition t:s.getTransitions())
				events.add(t.getEvent());
		for (Transition t:initialPseudoStateList)
			events.add(t.getEvent());
		for (Event e:events)
			for (Event ee:events)
				if ((e!=ee)&(e.getMessageType()==ee.getMessageType()))
					throw new RvgridException("Error in state machine design: "
						+ "events '" + e.getName()
						+ "' and '" + ee.getName()
						+ "' have the same message type '" + e.getMessageType()
						+ "'");
		return events;
	}

	// checks that all transitions go to states recorded in this state machine
	private void checkTransitions() {
		for (State s:stateList)
			for (Transition t:s.getTransitions())
				if (!stateList.contains(t.getToState()))
					throw new RvgridException("Error in state machine design: state '" + t.getToState().getName()
							+ "' in transition triggered by event '" + t.getEvent().getName()
							+ "' is unknown from the state machine");
	}

	// just make sure all constructors go through these steps
	private void getReallyReallyFinallyReady() {
		Set<Event> events = checkEvents();
		int[] eventix = new int[events.size()];
		int maxEventix = -Integer.MAX_VALUE;
		int i=0;
		for (Event e:events) {
			eventix[i] = e.getMessageType();
			if (eventix[i]>maxEventix)
				maxEventix = eventix[i];
			i++;
		}
		STATUS_MESSAGE = maxEventix+1;
		checkTransitions();
		addRendezvous(new StateTransitionProcess(),eventix);
		log.info(()->toString()+" initialised");
	}

	/**
	 * <p>Basic constructor with only one initial pseudo-state. All constructors perform 
	 * the following consistency checks:</p>
	 * <ol>
	 * <li>Events must have different message type indexes</li>
	 * <li>Transitions must refer to states present in the state machine</li>
	 * </ol>
	 * <p>Exceptions will be thrown if these checks fail.</p>
	 * 
	 * @param initialPseudoState the initial pseudo state (= a transition to one of the states)
	 * @param states the list of states
	 */
	public StateMachineEngine(Transition initialPseudoState, State... states) {
		super();
		for (State s:states) {
			s.seal();
			stateList.add(s);
		}
		initialPseudoStateList.add(initialPseudoState);
		getReallyReallyFinallyReady();
	}

	/**
	 * <p>Basic constructor with only one initial pseudo-state. All constructors perform 
	 * the following consistency checks:</p>
	 * <ol>
	 * <li>Events must have different message type indexes</li>
	 * <li>Transitions must refer to states present in the state machine</li>
	 * </ol>
	 * <p>Exceptions will be thrown if these checks fail.</p>
	 * 
	 * @param initialPseudoState the initial pseudo state (= a transition to one of the states)
	 * @param states the list of states
	 */
	public StateMachineEngine(Transition initialPseudoState, Iterable<State> states) {
		super();
		for (State s:states) {
			s.seal();
			stateList.add(s);
		}
		initialPseudoStateList.add(initialPseudoState);
		getReallyReallyFinallyReady();
	}

	/**
	 * <p>Generic constructor with many initial pseudo-states. All constructors perform 
	 * the following consistency checks:</p>
	 * <ol>
	 * <li>Events must have different message type indexes</li>
	 * <li>Transitions must refer to states present in the state machine</li>
	 * </ol>
	 * <p>Exceptions will be thrown if these checks fail.</p>
	 * 
	 * @param initialPseudoStates a list of initial pseudo state (= transitions to one of the states)
	 * @param states the list of states
	 */
	public StateMachineEngine(Iterable<Transition> initialPseudoStates, Iterable<State> states) {
		super();
		for (State s:states) {
			s.seal();
			stateList.add(s);
		}
		for (Transition ips:initialPseudoStates)
			initialPseudoStateList.add(ips);
		getReallyReallyFinallyReady();
	}

	// StateMachine

	@Override
	public Collection<State> getStates() {
		return Collections.unmodifiableCollection(stateList);
	}

	@Override
	public Collection<Transition> getInitialPseudoStates() {
		return Collections.unmodifiableCollection(initialPseudoStateList);
	}

	@Override
	public void setCurrentState(State state) {
		log.info(()->"Now entering state "+state.getName());
		currentState = state;
		// send new state to observers
		sendMessage(STATUS_MESSAGE,currentState);
	}

	@Override
	public State getCurrentState() {
		return currentState;
	}

	@Override
	public Collection<Transition> getTransitions() {
		List<Transition> result = new ArrayList<Transition>();
		for (State s : stateList)
			result.addAll(s.getTransitions());
		return Collections.unmodifiableCollection(result);
	}

	@Override
	public Collection<Event> getEvents() {
		List<Event> result = new ArrayList<Event>();
		for (Transition t : getTransitions())
			if (!result.contains(t.getEvent()))
				result.add(t.getEvent());
		for (Transition t : getInitialPseudoStates())
			if (!result.contains(t.getEvent()))
				result.add(t.getEvent());
		return Collections.unmodifiableCollection(result);
	}

	@Override
	public boolean inInitialState() {
		for (Transition t : getTransitions()) {
			if (t.getToState().equals(currentState))
				return false;
		}
		return true;
	}

	// Observable

	@Override
	public void addObserver(O listener) {
		stateMachineListenerNodeList.add(listener);
	}

	@Override
	public void sendMessage(int msgType, Object payload) {
		for (O l : stateMachineListenerNodeList) {
			log.info(()->"Sending status to observer "+l.toString());
			RVMessage statusMessage = new RVMessage(msgType,payload,this,l);
			l.callRendezvous(statusMessage);
		}
	}

	// Object

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName())
			.append(':')
			.append(this.hashCode())
			.append(" with ")
			.append(stateList.size())
			.append(" states = (");
		sb.append(stateList.get(0).getName());
		for (int i=1; i<stateList.size(); i++) {
			State s = stateList.get(i);
			sb.append(',').append(s.getName());
		}
		sb.append(')');
		return sb.toString();
	}

	// Local

	/**
	 * 
	 * @return a text description of the current state
	 */
	public String currentStateString() {
		String result = "[Current State";
		if (currentState == null)
			return INITIAL_PSEUDO_STATE;
		else {
			result = currentState.toString();
			if (inInitialState())
				result = result + " (initial state)";
			if (currentState.isQuiescent())
				result = result + " (quiescent state)";
			return result;
		}
	}

	/**
	 * Get a state by its name
	 * 
	 * @param name the state name
	 * @return the {@code State} instance
	 */
	public State findState(String name) {
		if (name == null || name.equals(INITIAL_PSEUDO_STATE)) {
			return null;
		} else {
			for (State s : stateList) {
				if (s.getName().equals(name))
					return s;
			}
		}
		throw new RvgridException("StateModel: Cannot find state named '" + name + "'");
	}

	/**
	 * The status message is sent to all observers at every state change. This code must be
	 * prefixed to all status messages.
	 * 
	 * @return the status message code
	 */
	public int statusMessageCode() {
		return STATUS_MESSAGE;
	}

	@Override
	public void removeObserver(O listener) {
		stateMachineListenerNodeList.remove(listener);
	}

	@Override
	public boolean hasObservers() {
		return !stateMachineListenerNodeList.isEmpty();
	}

	@Override
	public Collection<O> observers() {
		return Collections.unmodifiableCollection(stateMachineListenerNodeList);
	}

}
