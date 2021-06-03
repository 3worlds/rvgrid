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

import fr.cnrs.iees.rvgrid.observer.Observer;

/**
 * <p>The extension of {@link Observer} required by the {@link fr.cnrs.iees.rvgrid.rendezvous rendezvous pattern}.</p>
 * <p>Instances implementing this interface have the capability to call for a rendezvous on another 
 * {@code GridNode}.
 * </p>
 * 
 * <p>{@code GridNode}s can reside in different threads. When a rendezvous is called, data is transferred
 * to the other GridNode and an atomic (synchronized) action is executed. This enables inter-thread 
 * communication in a multi-thread context, or simple message passing in a single thread context.</p>
 * 
 * @author Jacques Gignoux - 14 août 2019
 *
 */
public interface GridNode extends Observer {

	/**
	 * Calls a rendezvous on any GridNode registering this rendezvous type. 
	 * <p><strong>CAUTION: implementations must be synchronized</strong></p>
	 *
	 * 
	 * @param message the rendezvous message to pass to the recipient
	 * @return this instance for agile programming
	 */
	public AbstractGridNode callRendezvous(RVMessage message);
	
}
