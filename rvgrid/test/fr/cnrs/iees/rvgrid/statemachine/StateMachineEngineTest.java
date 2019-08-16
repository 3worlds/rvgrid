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

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.rvgrid.RvgridException;
import fr.cnrs.iees.rvgrid.rendezvous.GridNode;
/**
 * Testing the 3Worlds simulator state machine (cf simulator-state-machine.dia)
 * 
 * 
 * @author Jacques Gignoux - 16 août 2019
 *
 */
public class StateMachineEngineTest {
	
	private State waiting, stepping,pausing,running,quitting,finished;
	private Transition ips;
	private Event run,step,reset,goOn,pause,finalise,quit,initialise;

	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}

	@BeforeEach
	private void init() {
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
	}

	@Test
	public final void testStateMachineImplTransitionStateArray() {
		StateMachine sm = new StateMachineEngine<GridNode>(ips,waiting, stepping,pausing,running,quitting,finished);
		assertNotNull(sm);
		// an incomplete state list should throw an exception:
		Exception e = assertThrows(RvgridException.class,
			()->new StateMachineEngine<GridNode>(ips,waiting,running));
		show("testStateMachineImplTransitionStateArray",e.toString());
	}

	@Test
	public final void testStateMachineImplTransitionIterableOfState() {
		Set<State> states = new HashSet<State>();
		states.add(waiting);
		states.add(stepping);
		states.add(pausing);
		states.add(running);
		states.add(quitting);
		states.add(finished);
		StateMachine sm = new StateMachineEngine<GridNode>(ips,states);
		assertNotNull(sm);
	}

	@Test
	public final void testStateMachineImplIterableOfTransitionIterableOfState() {
		List<Transition> ipss = new LinkedList<Transition>();
		ipss.add(ips);
		StateMachine sm = new StateMachineEngine<GridNode>(ips,waiting, stepping,pausing,running,quitting,finished);
		assertNotNull(sm);
		// state machine components cannot be changed once initialised
		Exception e = assertThrows(RvgridException.class,
			()->finished.addTransition(new Transition(quitting,step)));
		show("testStateMachineImplIterableOfTransitionIterableOfState",e.toString());
	}
	
	@Test
	public final void testStateMachineImplWrong1() {
		// two outgoing transitions triggered by the same event out of a state should throw an Exception
		finished.addTransition(new Transition(waiting,quit));
		Exception e = assertThrows(RvgridException.class,
			()->new StateMachineEngine<GridNode>(ips,waiting, stepping,pausing,running,quitting,finished));
		show("testStateMachineImplWrong1",e.toString());
	}

	@Test
	public final void testStateMachineImplWrong2() {
		// a wrong event index should throw an exception
		finished.addTransition(new Transition(stepping,new Event(2,"wrong index")));
		Exception e = assertThrows(RvgridException.class,
			()->new StateMachineEngine<GridNode>(ips,waiting, stepping,pausing,running,quitting,finished));
		show("testStateMachineImplWrong2",e.toString());
	}


	@Test
	public final void testGetEventCode() {
		StateMachine sm = new StateMachineEngine<GridNode>(ips,waiting, stepping,pausing,running,quitting,finished);
		show("testGetEventCode",sm.toString());
//		sm.getEventCode(step);
	}

	@Test
	public final void testGetStates() {
		StateMachine sm = new StateMachineEngine<GridNode>(ips,waiting, stepping,pausing,running,quitting,finished);
		show("testGetStates",sm.getStates().toString());
	}

	@Test
	public final void testGetInitialPseudoStates() {
		StateMachine sm = new StateMachineEngine<GridNode>(ips,waiting, stepping,pausing,running,quitting,finished);
		show("testGetInitialPseudoStates",sm.getInitialPseudoStates().toString());
	}

	@Test
	public final void testSetCurrentState() {
		fail("Not yet implemented");
	}

	@Test
	public final void testGetCurrentState() {
		fail("Not yet implemented");
	}

	@Test
	public final void testGetTransitions() {
		StateMachine sm = new StateMachineEngine<GridNode>(ips,waiting, stepping,pausing,running,quitting,finished);
		show("testGetTransitions",sm.getTransitions().toString());
	}

	@Test
	public final void testGetEvents() {
		StateMachine sm = new StateMachineEngine<GridNode>(ips,waiting, stepping,pausing,running,quitting,finished);
		show("testGetEvents",sm.getEvents().toString());
	}

	@Test
	public final void testInInitialState() {
		fail("Not yet implemented");
	}

	@Test
	public final void testAddObserver() {
		fail("Not yet implemented");
	}

	@Test
	public final void testSendMessage() {
		fail("Not yet implemented");
	}

	@Test
	public final void testCurrentStateString() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindState() {
		fail("Not yet implemented");
	}

}
