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
 * <p>An example 'controller' node.</p> 
 * <p>This node understands two message types, {@link Main#MSG_SIM_TO_CTRL1} and
 * {@link Main#MSG_SIM_TO_CTRL2} (cf. constructor). </p>
 * @author Jacques Gignoux - 9 août 2021
 *
 */
public class CtrlNode extends AbstractGridNode implements Observer, Observable<SimNode> {
	private List<SimNode> ctrlListeners;

	/**
	 * This constructors sets a Rendezvous using a single {@link RendezvousProcess} in
	 * response to its two message types.
	 */
	public CtrlNode () {
		ctrlListeners = new ArrayList<>();
		this.addRendezvous(new RendezvousProcess() {

			@Override
			public void execute(RVMessage message) {
				myProcess(message);
				
			}}, Main.MSG_SIM_TO_CTRL1, Main.MSG_SIM_TO_CTRL2);
		

	}

	private void myProcess (RVMessage msg) {
		System.out.println("Ctrl msg process: " + msg.getMessageHeader().type());

	}
	@Override
	public void addObserver(SimNode l) {
		ctrlListeners.add(l);
	}
	
	@Override
	public void sendMessage(int msgType, Object payload) {
		for (GridNode target : ctrlListeners) {
			RVMessage msg = new RVMessage(msgType, payload,this,target);
			target.callRendezvous(msg);
		}
	}

	@Override
	public void removeObserver(SimNode listener) {
		ctrlListeners.remove(listener);
	}

	@Override
	public boolean hasObservers() {
		return !ctrlListeners.isEmpty();
	}

	@Override
	public Collection<SimNode> observers() {
		return Collections.unmodifiableCollection(ctrlListeners);
	}
}
