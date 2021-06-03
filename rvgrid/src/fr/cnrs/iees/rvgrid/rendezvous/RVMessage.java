/**************************************************************************
 *  RVGRID - A light-weight rendezvous system                             *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          * 
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  RVGRID is a light-weight implementation of ADA's rendez-vous          *
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

/**
 * <p>Data exchanged between two {@link GridNode}s at rendezvous.</p>
 * <p>Messages contain a type that is used by the {@code GridNode} to decide which {@link RendezvousProcess}
 * must be activated to process them at a rendezvous, i.e. when 
	 * {@code GridNode}.{@link GridNode#callRendezvous(RVMessage) callRendezvous(...)} is called. 
 * This class can be used as is or may have descendants for specific applications.</p>
 * 
 * @author Ian Davies - 14 août 2019<br/>
 * 			after Shayne Flint, 2012
 *
 */
public class RVMessage {
	private RVMessageHeader messageHeader;
	private Object payload;

	/**
	 * Messages must be constructed knowing their type, caller and receiver. They can carry information
	 * (payload) of any kind.
	 * 
	 * @param type the type of {@link RendezvousProcess} to call at rendezvous
	 * @param payload any additional information to pass to the 
	 *    {@code RendezvousProcess}.{@link RendezvousProcess#execute(RVMessage) execute(...)} method
	 * @param source the caller {@code GridNode}
	 * @param target the receiver {@code GridNode}
	 */
	public RVMessage(int type,Object payload, GridNode source, GridNode target) {
		this.messageHeader = new RVMessageHeader(type,source,target);
		this.payload=payload;
	}

	/**
	 * Getter for the {@link RVMessageHeader} which contains the caller, receiver and type information
	 * @return the {@code RVMessageHeader} for this message
	 */
	public RVMessageHeader getMessageHeader() {
		return messageHeader;
	}
	
	/**
	 * Getter for the additional information contained in the message.
	 * @return the additional information to pass to the 
	 *    {@code RendezvousProcess}.{@link RendezvousProcess#execute(RVMessage) execute(...)} method
	 */
	public Object payload() {
		return payload;
	}

}
