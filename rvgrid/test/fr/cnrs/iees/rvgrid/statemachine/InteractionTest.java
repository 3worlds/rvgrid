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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.cnrs.iees.rvgrid.rendezvous.GridNode;

/**
 * A test case for a state machine in interaction with a controller / observer
 * 
 * @author Jacques Gignoux - 16 août 2019
 *
 */
// Whoohooh! works very well!
public class InteractionTest {

	public InteractionTest() {
	}
	
	public static void main(String[] args) {
		State waiting, stepping,pausing,running,quitting,finished;
		Transition ips;
		Event run,step,reset,goOn,pause,finalise,quit,initialise;
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
		StateMachineEngine<GridNode> sm = new StateMachineEngine<GridNode>(ips,waiting, stepping,pausing,running,quitting,finished);
		StateMachineObserver obs = new StateMachineObserver(sm);
		sm.addObserver(obs);
		String commandList = "exit,"+run.getName()+","
			+step.getName()+","
			+goOn.getName()+","
			+reset.getName()+","
			+pause.getName()+","
			+finalise.getName()+","
			+quit.getName()+","
			+initialise.getName();
		List<Event> events = new ArrayList<>();
		events.add(run); events.add(step); events.add(reset);
		events.add(goOn); events.add(pause); events.add(finalise);
		events.add(quit); events.add(initialise); 
		
		String userChoice = "";
		while (!userChoice.equals("exit")) {
		System.out.print("Command ("+commandList+")? ");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			userChoice = br.readLine();
			if (userChoice.equals("exit")) {
				System.out.println("finished playing with you.");
				System.exit(0);
			}
			for (Event e:events) {
				if (e.getName().equals(userChoice)) { 
					obs.sendEvent(e);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		}

		
	}
	

}
