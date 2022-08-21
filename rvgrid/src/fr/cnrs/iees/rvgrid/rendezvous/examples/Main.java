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
package fr.cnrs.iees.rvgrid.rendezvous.examples;

/**
 * <p>Use this class {@code main(...)} method to run a very minimalistic example of 
 * use of the Rendezvous pattern as implemented here.</p>
 * <p>The following actions are performed:</p>
 * <ul>
 * <li>Four message types (static integers) are defined;</li>
 * <li>Instance of {@link SimNode} and {@link CtrlNode} are created;</li>
 * <li>Each instance records the other as an {@link fr.cnrs.iees.rvgrid.observer.Observer Observer};</li>
 * <li>Four threads, each sending 10 messages between the two nodes, are instantiated;</li>
 * <li>The four threads are started;</li>
 * <li>Each node prints a brief text in the console when it receives a message.</li>
 * </ul>
 * 
 */

public class Main {
	/** Message type 1 from controller to simulator */
	public static int MSG_CTRL_TO_SIM1 = 10;
	/** Message type 2 from controller to simulator */
	public static int MSG_CTRL_TO_SIM2 = 11;
	/** Message type 1 from simulator to controller */
	public static int MSG_SIM_TO_CTRL1 = 20;
	/** Message type 2 from simulator to controller */
	public static int MSG_SIM_TO_CTRL2 = 21;

	/**
	 * Run or debug to see the pattern in action.
	 * @param args not used.
	 */
	public static void main(String[] args) {
		SimNode sim = new SimNode();
		CtrlNode ctrl = new CtrlNode();
		ctrl.addObserver(sim);
		sim.addObserver(ctrl);

		int nMsgs = 10;
		Object payload = new Object();

		Runnable task1 = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < nMsgs; i++)
					ctrl.sendMessage(MSG_CTRL_TO_SIM1, payload);
			}
		};
		Thread thread1 = new Thread(task1);
		Runnable task3 = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < nMsgs; i++)
					ctrl.sendMessage(MSG_CTRL_TO_SIM2, payload);

			}
		};
		Thread thread3 = new Thread(task3);

		Runnable task2 = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < nMsgs; i++)
					sim.sendMessage(MSG_SIM_TO_CTRL1, payload);

			}
		};
		Thread thread2 = new Thread(task2);

		Runnable task4 = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < nMsgs; i++)
					sim.sendMessage(MSG_SIM_TO_CTRL2, payload);

			}
		};
		Thread thread4 = new Thread(task4);

		thread1.start();
		thread3.start();
		thread4.start();
		thread2.start();

	}

}
