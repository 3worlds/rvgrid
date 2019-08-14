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

public class CtrlNode extends GridNodeImpl {
	private List<GridNode> ctrlListeners;

	public CtrlNode () {
		ctrlListeners = new ArrayList<>();
		this.addRendezvous(new RendezvousProcess() {

			@Override
			public void execute(RVMessage message) {
				myProcess(message);
				
			}}, Main.MSG_SIM_TO_CTRL1, Main.MSG_SIM_TO_CTRL2);
		

	}
	public void addCtrlListener(GridNode l) {
		ctrlListeners.add(l);
	}

	public void sendCtrlMessage(int type, Object payload) {
		for (GridNode target : ctrlListeners) {
			RVMessage msg = new RVMessage(type, payload,this,target);
			target.callRendezvous(msg);
		}
	}
	private void myProcess (RVMessage msg) {
		System.out.println("Ctrl msg process: " + msg.getMessageHeader().type());

	}
}
