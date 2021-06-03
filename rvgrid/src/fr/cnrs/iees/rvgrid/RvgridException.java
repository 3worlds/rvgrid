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
package fr.cnrs.iees.rvgrid;

import fr.ens.biologie.generic.Textable;

/**
 * The {@link java.lang.Exception Exception} class specific to this library.
 * 
 * @author Jacques Gignoux - 14 août 2019
 *
 */
public class RvgridException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1724666389814181944L;

	/**
	 * Instantiate an exception on an object with a message
	 * @param item the item which caused the problem
	 * @param message the error message
	 */
	public RvgridException(Textable item, String message) {
		super("[on " + item + "]\n[" + message + "]");
	}

	/**
	 * Instantiate an exception with a message
	 * @param message the error message
	 */
	public RvgridException(String message) {
		super("[" + message + "]");
	}

	/**
	 * Exception wrapper.
	 * @param e the exception to wrap
	 */
	public RvgridException(Exception e) {
		super(e);
	}

	/**
	 * Exception wrapper with additional information
	 * @param message the error message
	 * @param e the exception to wrap
	 */
	public RvgridException(String message, Exception e) {
		super("[" + message + "]\n[original exception: " + e + "]");
		e.printStackTrace();
	}


}
