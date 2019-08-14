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
import java.util.List;

import fr.cnrs.iees.rvgrid.rendezvous.GridNode;
import fr.cnrs.iees.rvgrid.rendezvous.GridNodeImpl;
import fr.cnrs.iees.rvgrid.rendezvous.RVMessage;
import fr.cnrs.iees.rvgrid.rendezvous.RendezvousProcess;

public class SimNode extends GridNodeImpl {
	private List<GridNode> simListeners;

	public SimNode() {
		simListeners = new ArrayList<>();
		this.addRendezvous(new RendezvousProcess() {

			@Override
			public void execute(RVMessage message) {
				myProcess(message);
				
			}}, Main.MSG_CTRL_TO_SIM1, Main.MSG_CTRL_TO_SIM2);
		
	}

	public void addSimListener(GridNode l) {
		simListeners.add(l);
	}

	public void sendSimListenerMessage(int type, Object payload) {
		for (GridNode target : simListeners) {
			RVMessage msg = new RVMessage(type, payload,this,target);
			target.callRendezvous(msg);
		}
	}
	private void myProcess (RVMessage msg) {
		System.out.println("Sim msg process: " + msg.getMessageHeader().type());
	
	}

}
