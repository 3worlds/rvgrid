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
package fr.cnrs.iees.rvgrid.rendezvous;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Instances of this class can exchange RVMessage at rendezvous and execute a RendezVousProcess
 * matching the message type.
 * 
 * @author Ian Davies - 14 août 2019
 * 			after Shayne Flint, 2012
 *
 */
public class GridNodeImpl implements GridNode {
	// used only when msgs bank up because rendezvous is yet to be added.
	private Queue<RVMessage> messageQueue = new LinkedList<>();
	private Map<Integer, RendezvousProcess> rendezvousProcesses = new HashMap<>();;

	public GridNodeImpl addRendezvous(RendezvousProcess process, int... types) {
		/**
		 * Duplicate the process entry for each type. Not sure when this would be the case
		 * unless a process uses a switch statement on msg type?
		 */
		for (int type : types)
			rendezvousProcesses.put(type, process);
		// Process any pending msgs if possible.
		if (!messageQueue.isEmpty()) {
			return callRendezvous(messageQueue.remove());
		}
		return this;
	}

	public synchronized GridNodeImpl callRendezvous(RVMessage message) {
		RendezvousProcess process = rendezvousProcesses.get(message.getMessageHeader().type());
		if (process == null)
			messageQueue.offer(message);
		else
			process.execute(message);
		return this;
	}

}
