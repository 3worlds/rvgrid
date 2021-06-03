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
package fr.cnrs.iees.rvgrid.observer;

import java.util.Collection;

/**
 * A class that can send messages about its state to {@link Observer}s of the specified type.
 * Internally, it maintains a dynamic list of registered {@code Observer}s to send messages to.
 * 
 * @author Jacques Gignoux - 14 août 2019
 *
 * @param <P> The {@code Observer} subclass
 */
public interface Observable<P extends Observer> {

	/**
	 * Registers an {@code Observer} to this instance
	 * 
	 * @param listener the Observer to add
	 */
	public void addObserver(P listener);
	
	/**
	 * Sends a message to all registered observers. Messages are typed so that different actions
	 * can be triggered in the observer, based on type.
	 * 
	 * @param msgType the message type
	 * @param payload a payload to send to all observers
	 */
	public void sendMessage(int msgType, Object payload);
	
	/**
	 * Removes an {@code Observer} from its list 
	 * @param listener the Observer instance to remove
	 */
	public void removeObserver(P listener);

	/**
	 * Check if there are registered observers.
	 * 
	 * @return {@code true} if there is at least one registered observer
	 */
	public boolean hasObservers();
	
	/**
	 * Get the list of registered observers. 
	 * 
	 * @return the immutable list of registered observers
	 */
	public Collection<P> observers();

}
