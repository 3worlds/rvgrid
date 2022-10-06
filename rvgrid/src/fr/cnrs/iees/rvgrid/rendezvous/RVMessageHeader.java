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

/**
 * Message header for rendezvous messages.
 * <p>
 * This class is {@code final} and can only be created from
 * {@link RVMessage#RVMessage(int, Object, GridNode, GridNode) RVMessage(...)}.
 * 
 * @author Ian Davies - 14 août 2019<br/>
 *         after Shayne Flint, 2012
 *
 */
public final class RVMessageHeader {
	private static int lastType = 0;

	/**
	 * Generates a unique int for message header types on request from any source.
	 * <p>
	 * The result should be held by the caller as a {@code final static} value.
	 * 
	 * @return Unique int.
	 */
	public static int createUniqueMessageHeaderType() {
		if (lastType == Integer.MAX_VALUE)
			throw new IllegalStateException("Generating unique message header types has overflowed.");
		int newType = lastType + 1;
		lastType = newType;
		return lastType;
	}

	private int type;
	private GridNode source, target;

	protected RVMessageHeader(int type, GridNode source, GridNode target) {
		this.type = type;
		this.source = source;
		this.target = target;
	}

	/**
	 * The type of the {@code RendezvousProcess} to execute at rendezvous.
	 * 
	 * @return the type
	 */
	public int type() {
		return type;
	}

	/**
	 * The caller {@code GridNode}.
	 * 
	 * @return the sender of this message
	 */
	public GridNode source() {
		return source;
	}

	/**
	 * The {@code GridNode} to which this message is sent
	 * 
	 * @return the receiver of this message
	 */
	public GridNode target() {
		return target;
	}
}
