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
 * Messages exchanged between two GridNodes at rendezvous.
 * May have descendants
 * 
 * @author Ian Davies - 14 août 2019
 * 			after Shayne Flint, 2012
 *
 */
public class RVMessage {
	private RVMessageHeader messageHeader;
	private Object payload;

	public RVMessage(int type,Object payload, GridNode source, GridNode target) {
		this.messageHeader = new RVMessageHeader(type,source,target);
		this.payload=payload;
	}

	public RVMessageHeader getMessageHeader() {
		return messageHeader;
	}
	
	public Object payload() {
		return payload;
	}

}
