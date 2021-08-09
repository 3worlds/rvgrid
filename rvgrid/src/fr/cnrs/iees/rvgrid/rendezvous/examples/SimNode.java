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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fr.cnrs.iees.rvgrid.rendezvous.GridNode;
import fr.cnrs.iees.rvgrid.observer.Observable;
import fr.cnrs.iees.rvgrid.observer.Observer;
import fr.cnrs.iees.rvgrid.rendezvous.AbstractGridNode;
import fr.cnrs.iees.rvgrid.rendezvous.RVMessage;
import fr.cnrs.iees.rvgrid.rendezvous.RendezvousProcess;

/**
 * <p>An example 'simulator' node.</p> 
 * <p>This node understands two message types, {@link MSG_CTRL_TO_SIM1} and
 * {@link MSG_CTRL_TO_SIM2} (cf. constructor). </p>
 * @author Jacques Gignoux - 9 août 2021
 *
 */
public class SimNode extends AbstractGridNode
		implements Observable<CtrlNode>, Observer {
	private List<CtrlNode> simListeners;

	/**
	 * This constructors sets a Rendezvous using a single {@link RendezvousProcess} in
	 * response to its two message types.
	 */
	public SimNode() {
		simListeners = new ArrayList<>();
		this.addRendezvous(new RendezvousProcess() {

			@Override
			public void execute(RVMessage message) {
				myProcess(message);
				
			}}, Main.MSG_CTRL_TO_SIM1, Main.MSG_CTRL_TO_SIM2);
		
	}

	private void myProcess (RVMessage msg) {
		System.out.println("Sim msg process: " + msg.getMessageHeader().type());
	
	}

	@Override
	public void addObserver(CtrlNode l) {
		simListeners.add(l);
	}

	@Override
	public void sendMessage(int msgType, Object payload) {
		for (GridNode target : simListeners) {
			RVMessage msg = new RVMessage(msgType, payload,this,target);
			target.callRendezvous(msg);
		}
	}

	@Override
	public void removeObserver(CtrlNode listener) {
		simListeners.remove(listener);
	}

	@Override
	public boolean hasObservers() {
		return !simListeners.isEmpty();
	}

	@Override
	public Collection<CtrlNode> observers() {
		return Collections.unmodifiableCollection(simListeners);
	}

}
