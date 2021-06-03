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
 * <p>Implementation of {@link GridNode}.</p>
 * 
 * <p>Instances of this class can exchange {@link RVMessage} at rendezvous and execute a 
 * {@link RendezvousProcess} matching the message type.</p>
 * 
 * <p>Instances of this class register types of messages matching particular actions 
 * (= instances of {@link RendezvousProcess}) through the {@code addRendezvous} method. 
 * These types are those that this instance are able
 * to process.</p>
 * <p>Whenever a message is received (through a call to the {@code callRendezvous} method), it is 
 * checked against message types: if the type is already registered, the matching {@code RendezvousProcess}
 * is executed; if not, the message is put in a queue for possible later registering of this message
 * type. The next time  {@code addRendezvous} is called, it will attempt to process all messages
 * contained in the queue.
 * </p>
 * <p>Of course one should be careful to always call {@code callRendezvous} with a message type
 * that is or will be registered with this instance; otherwise messages can pile up indefinitely.</p>
 * 
 * <p>This class should be overriden by specialised descendants for any particular implementation
 * of the {@link fr.cnrs.iees.rvgrid.rendezvous rendezvous pattern}.</p>
 * 
 * @author Ian Davies - 14 août 2019<br/>
 * 			after Shayne Flint, 2012
 *
 */
public abstract class AbstractGridNode implements GridNode {
	// used only when msgs bank up because rendezvous is yet to be added.
	protected Queue<RVMessage> messageQueue = new LinkedList<>();
	private Map<Integer, RendezvousProcess> rendezvousProcesses = new HashMap<>();

	/**
	 * Registers a message type in this instance
	 * 
	 * @param process the action to undertake on a rendezvous
	 * @param types the type attached to the process
	 * @return this instance for agile programming
	 */
	public AbstractGridNode addRendezvous(RendezvousProcess process, int... types) {
		/**
		 * Duplicate the process entry for each type. Not sure when this would be the case
		 * unless a process uses a switch statement on msg type?
		 * yes, the use case works if payloads are different
		 */
		for (int type : types)
			rendezvousProcesses.put(type, process);
		// Process any pending msgs if possible.
		if (!messageQueue.isEmpty()) {
			return callRendezvous(messageQueue.remove());
		}
		return this;
	}

	/**
	 * This method implements a simple message passing that works for single or multi-thread applications.
	 * It has been made {@code final} as it is very sensitive code and should not be overriden 
	 * unless you are a specialist of the rendezvous pattern. In case you want to implement
	 * inter-process capabilities, we suggest you define a sister class to this one with the
	 * <em>adhoc</em> {@code callRendezVous} method.
	 */
	@Override
	public final synchronized AbstractGridNode callRendezvous(RVMessage message) {
		RendezvousProcess process = rendezvousProcesses.get(message.getMessageHeader().type());
		if (process == null)
			messageQueue.offer(message);
		else
			process.execute(message);
		return this;
	}

}
